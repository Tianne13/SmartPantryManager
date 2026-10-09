package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// main screen of my app, it shows the list of pantry items
public class MainActivity extends AppCompatActivity {

    // the list and the "pantry is empty" message from activity_main.xml
    RecyclerView rvPantry;
    TextView tvEmptyMessage;

    // I made this a variable of the class so the click listeners can use it too
    ArrayList<PantryItem> pantryItems;

    // runs when a row in the list is tapped, it opens the form in edit mode
    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // find out which row was tapped
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            PantryItem item = pantryItems.get(position);

            // send the id of the item to the form using the intent
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            intent.putExtra("itemId", item.getId());
            startActivity(intent);
        }
    };

    // runs when the Delete button on a row is tapped
    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            PantryItem item = pantryItems.get(position);

            // ask the user to confirm before deleting
            confirmDelete(item);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // connecting my variables to the views in the layout
        rvPantry = findViewById(R.id.rvPantry);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        initNavigationBar();

        // when the Add Ingredient button is clicked, open the add form
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the intent tells android which screen to open next
                Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
                startActivity(intent);
            }
        });
    }

    // the navigation bar at the bottom of the screen
    private void initNavigationBar() {
        Button btnNavPantry = findViewById(R.id.btnNavPantry);
        Button btnNavRecipes = findViewById(R.id.btnNavRecipes);
        Button btnNavSettings = findViewById(R.id.btnNavSettings);

        // I am already on this screen so this button is switched off
        btnNavPantry.setEnabled(false);

        btnNavRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
            // clear top stops android from making lots of copies of the same screen
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnNavSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    // onResume runs every time the screen shows up again,
    // so the list refreshes after I add, edit or delete something
    @Override
    protected void onResume() {
        super.onResume();
        loadPantryList();
    }

    // gets the items from the database and puts them in the list
    private void loadPantryList() {
        PantryDataSource dataSource = new PantryDataSource(this);

        try {
            dataSource.open();
            pantryItems = dataSource.getAllPantryItems();
            dataSource.close();

            if (pantryItems.size() == 0) {
                // no items yet so show the message and hide the list
                tvEmptyMessage.setVisibility(View.VISIBLE);
                rvPantry.setVisibility(View.GONE);
            } else {
                // there are items so hide the message and show the list
                tvEmptyMessage.setVisibility(View.GONE);
                rvPantry.setVisibility(View.VISIBLE);

                // layout manager makes it a normal vertical list
                rvPantry.setLayoutManager(new LinearLayoutManager(this));
                PantryAdapter adapter = new PantryAdapter(pantryItems);

                // give the adapter the click listeners BEFORE setting the adapter
                adapter.setOnItemClickListener(itemClickListener);
                adapter.setOnDeleteClickListener(deleteClickListener);
                rvPantry.setAdapter(adapter);
            }
        } catch (Exception e) {
            // something went wrong with the database
            Toast.makeText(this, "Error retrieving pantry items", Toast.LENGTH_LONG).show();
        }
    }

    // shows a pop-up asking if the user is sure they want to delete
    private void confirmDelete(PantryItem item) {
        final PantryItem itemToDelete = item;

        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Are you sure you want to delete " + item.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteItem(itemToDelete))
                .setNegativeButton("Cancel", null)
                .show();
    }

    // removes the item from the database and refreshes the list
    private void deleteItem(PantryItem item) {
        PantryDataSource dataSource = new PantryDataSource(this);
        boolean didDelete = false;

        try {
            dataSource.open();
            didDelete = dataSource.deletePantryItem(item.getId());
            dataSource.close();
        } catch (Exception e) {
            didDelete = false;
        }

        if (didDelete) {
            Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
            loadPantryList(); // refresh so the item disappears
        } else {
            Toast.makeText(this, "Could not delete the ingredient", Toast.LENGTH_LONG).show();
        }
    }
}