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

// this screen shows only the recipes I can make with what is in my pantry
public class SuggestedRecipesActivity extends AppCompatActivity {

    RecyclerView rvRecipes;
    TextView tvNoRecipes;

    // the recipes that passed the strict matching
    ArrayList<Recipe> suggestedRecipes;

    // runs when a recipe in the list is tapped
    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // find out which row was tapped
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            Recipe recipe = suggestedRecipes.get(position);

            // send the recipe id to the detail screen using the intent
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // connecting my variables to the views in the layout
        rvRecipes = findViewById(R.id.rvRecipes);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);

        initNavigationBar();
    }

    // the navigation bar at the bottom of the screen
    private void initNavigationBar() {
        Button btnNavPantry = findViewById(R.id.btnNavPantry);
        Button btnNavRecipes = findViewById(R.id.btnNavRecipes);
        Button btnNavSettings = findViewById(R.id.btnNavSettings);

        // I am already on this screen so this button is switched off
        btnNavRecipes.setEnabled(false);

        btnNavPantry.setOnClickListener(v -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, MainActivity.class);
            // clear top stops android from making lots of copies of the same screen
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnNavSettings.setOnClickListener(v -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    // onResume runs every time the screen shows, so the list is checked against
    // the pantry again each time
    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    // runs the strict matching and shows the recipes that passed
    private void loadSuggestedRecipes() {
        PantryDataSource dataSource = new PantryDataSource(this);

        try {
            dataSource.open();
            // the strict matching logic is in RecipeMatcher
            suggestedRecipes = RecipeMatcher.getSuggestedRecipes(dataSource);
            dataSource.close();

            if (suggestedRecipes.size() == 0) {
                // nothing matches so show the message and hide the list
                tvNoRecipes.setVisibility(View.VISIBLE);
                rvRecipes.setVisibility(View.GONE);
            } else {
                tvNoRecipes.setVisibility(View.GONE);
                rvRecipes.setVisibility(View.VISIBLE);

                rvRecipes.setLayoutManager(new LinearLayoutManager(this));
                RecipeAdapter adapter = new RecipeAdapter(suggestedRecipes);

                // give the adapter the click listener BEFORE setting the adapter
                adapter.setOnItemClickListener(itemClickListener);
                rvRecipes.setAdapter(adapter);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading recipes", Toast.LENGTH_LONG).show();
        }
    }
}