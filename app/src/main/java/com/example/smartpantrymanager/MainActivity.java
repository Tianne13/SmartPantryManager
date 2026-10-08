package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

    // onResume runs every time the screen shows up again,
    // so the list refreshes after I add something and come back
    @Override
    protected void onResume() {
        super.onResume();
        loadPantryList();
    }

    // gets the items from the database and puts them in the list
    private void loadPantryList() {
        PantryDataSource dataSource = new PantryDataSource(this);
        ArrayList<PantryItem> pantryItems;

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
                rvPantry.setAdapter(adapter);
            }
        } catch (Exception e) {
            // something went wrong with the database
            Toast.makeText(this, "Error retrieving pantry items", Toast.LENGTH_LONG).show();
        }
    }
}