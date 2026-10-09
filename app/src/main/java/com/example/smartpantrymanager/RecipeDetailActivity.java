package com.example.smartpantrymanager;

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

import java.util.ArrayList;

// this screen shows one recipe with all its ingredients and the method
public class RecipeDetailActivity extends AppCompatActivity {

    TextView tvDetailName;
    TextView tvDetailIngredients;
    TextView tvDetailSteps;
    Button btnDetailBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // connecting my variables to the views in the layout
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailIngredients = findViewById(R.id.tvDetailIngredients);
        tvDetailSteps = findViewById(R.id.tvDetailSteps);
        btnDetailBack = findViewById(R.id.btnDetailBack);

        // back button closes this screen and goes back to the recipes list
        btnDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // get the recipe id that the recipes list sent with the intent
        int recipeId = -1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeId = extras.getInt("recipeId", -1);
        }

        if (recipeId == -1) {
            // no id was sent so there is nothing to show
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_LONG).show();
            finish();
        } else {
            loadRecipe(recipeId);
        }
    }

    // gets the recipe and its ingredients from the database and shows them
    private void loadRecipe(int recipeId) {
        PantryDataSource dataSource = new PantryDataSource(this);

        try {
            dataSource.open();
            Recipe recipe = dataSource.getRecipe(recipeId);
            ArrayList<RecipeIngredient> ingredients =
                    dataSource.getIngredientsForRecipe(recipeId);
            dataSource.close();

            tvDetailName.setText(recipe.getName());
            tvDetailSteps.setText(recipe.getSteps());

            // build one text with every ingredient on its own line
            String ingredientText = "";
            for (RecipeIngredient ingredient : ingredients) {
                ingredientText = ingredientText + "• "
                        + formatQuantity(ingredient.getQuantity()) + " "
                        + ingredient.getUnit() + " "
                        + ingredient.getIngredientName() + "\n";
            }
            tvDetailIngredients.setText(ingredientText.trim());

        } catch (Exception e) {
            Toast.makeText(this, "Error loading the recipe", Toast.LENGTH_LONG).show();
        }
    }

    // shows 3 instead of 3.0 when the number is a whole number
    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((int) quantity);
        }
        return String.valueOf(quantity);
    }
}