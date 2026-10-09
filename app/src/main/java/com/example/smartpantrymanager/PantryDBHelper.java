package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// this class creates the database and its tables, and puts the starting recipes in
public class PantryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pantry.db";

    // I changed this from 1 to 2 so android rebuilds the database with the recipes in it
    private static final int DATABASE_VERSION = 2;

    // sql to create the pantry_items table
    private static final String CREATE_TABLE_PANTRY =
            "CREATE TABLE pantry_items ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "quantity REAL, "
                    + "unit TEXT, "
                    + "expiry_date TEXT);";

    // sql to create the recipes table
    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE recipes ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "steps TEXT);";

    // sql to create the recipe_ingredients table
    // recipe_id says which recipe each ingredient belongs to
    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "CREATE TABLE recipe_ingredients ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "recipe_id INTEGER, "
                    + "ingredient_name TEXT NOT NULL, "
                    + "quantity REAL, "
                    + "unit TEXT);";

    public PantryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // runs the first time the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PANTRY);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);

        // put the starting recipes into the database
        seedRecipes(db);
    }

    // runs when the version number goes up, it deletes the old tables and builds them again
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PantryDBHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS pantry_items");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        onCreate(db);
    }

    // adds one recipe to the recipes table and gives back its new id
    private long addRecipe(SQLiteDatabase db, String name, String steps) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("steps", steps);
        return db.insert("recipes", null, values);
    }

    // adds one ingredient to a recipe, using the recipe's id
    private void addIngredient(SQLiteDatabase db, long recipeId, String name,
                               double quantity, String unit) {
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("ingredient_name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        db.insert("recipe_ingredients", null, values);
    }

    // puts all the starting recipes and their ingredients into the database
    private void seedRecipes(SQLiteDatabase db) {
        long id;

        // 1
        id = addRecipe(db, "Scrambled Eggs",
                "1. Crack the eggs into a bowl and whisk with the salt.\n"
                        + "2. Melt the butter in a pan on medium heat.\n"
                        + "3. Pour in the eggs and stir slowly until they are set.\n"
                        + "4. Serve hot.");
        addIngredient(db, id, "egg", 3, "pcs");
        addIngredient(db, id, "butter", 10, "g");
        addIngredient(db, id, "salt", 1, "tsp");

        // 2
        id = addRecipe(db, "Cheese Toast",
                "1. Butter one side of each slice of bread.\n"
                        + "2. Put the cheese between the slices with the butter on the outside.\n"
                        + "3. Fry in a pan until golden on both sides and the cheese has melted.");
        addIngredient(db, id, "bread", 2, "pcs");
        addIngredient(db, id, "cheese", 50, "g");
        addIngredient(db, id, "butter", 10, "g");

        // 3
        id = addRecipe(db, "Tomato Pasta",
                "1. Boil the pasta in salted water until soft, then drain.\n"
                        + "2. Chop the onion, garlic and tomatoes.\n"
                        + "3. Fry the onion and garlic in the oil for 3 minutes.\n"
                        + "4. Add the tomatoes and cook for 10 minutes.\n"
                        + "5. Mix the sauce with the pasta and serve.");
        addIngredient(db, id, "pasta", 200, "g");
        addIngredient(db, id, "tomato", 3, "pcs");
        addIngredient(db, id, "onion", 1, "pcs");
        addIngredient(db, id, "garlic", 2, "pcs");
        addIngredient(db, id, "oil", 2, "tbsp");
        addIngredient(db, id, "salt", 1, "tsp");

        // 4
        id = addRecipe(db, "Egg Fried Rice",
                "1. Cook the rice and let it cool down.\n"
                        + "2. Chop the onion and fry it in the oil for 3 minutes.\n"
                        + "3. Push the onion aside and scramble the eggs in the same pan.\n"
                        + "4. Add the rice and salt and stir fry for 5 minutes.");
        addIngredient(db, id, "rice", 150, "g");
        addIngredient(db, id, "egg", 2, "pcs");
        addIngredient(db, id, "onion", 1, "pcs");
        addIngredient(db, id, "oil", 1, "tbsp");
        addIngredient(db, id, "salt", 1, "tsp");

        // 5
        id = addRecipe(db, "Pancakes",
                "1. Mix the flour, sugar, egg and milk into a smooth batter.\n"
                        + "2. Melt a little butter in a pan.\n"
                        + "3. Pour in some batter and cook until bubbles show, then flip.\n"
                        + "4. Repeat until the batter is finished.");
        addIngredient(db, id, "flour", 150, "g");
        addIngredient(db, id, "egg", 1, "pcs");
        addIngredient(db, id, "milk", 250, "ml");
        addIngredient(db, id, "sugar", 2, "tbsp");
        addIngredient(db, id, "butter", 20, "g");

        // 6
        id = addRecipe(db, "Mashed Potatoes",
                "1. Peel and chop the potatoes.\n"
                        + "2. Boil them in salted water for 15 to 20 minutes until soft.\n"
                        + "3. Drain and mash with the butter and milk.");
        addIngredient(db, id, "potato", 4, "pcs");
        addIngredient(db, id, "butter", 30, "g");
        addIngredient(db, id, "milk", 100, "ml");
        addIngredient(db, id, "salt", 1, "tsp");

        // 7
        id = addRecipe(db, "Banana Oat Porridge",
                "1. Put the oats and milk in a pot on low heat.\n"
                        + "2. Stir for about 5 minutes until thick.\n"
                        + "3. Slice the banana on top and add the honey.");
        addIngredient(db, id, "oat", 80, "g");
        addIngredient(db, id, "milk", 300, "ml");
        addIngredient(db, id, "banana", 1, "pcs");
        addIngredient(db, id, "honey", 1, "tbsp");

        // 8
        id = addRecipe(db, "Tuna Sandwich",
                "1. Mix the tuna with the mayonnaise in a bowl.\n"
                        + "2. Spread it on one slice of bread.\n"
                        + "3. Put the other slice on top and cut in half.");
        addIngredient(db, id, "bread", 2, "pcs");
        addIngredient(db, id, "tuna", 100, "g");
        addIngredient(db, id, "mayonnaise", 1, "tbsp");

        // 9
        id = addRecipe(db, "Cheese Omelette",
                "1. Whisk the eggs in a bowl.\n"
                        + "2. Melt the butter in a pan and pour in the eggs.\n"
                        + "3. Sprinkle the cheese on one half when the eggs start to set.\n"
                        + "4. Fold the omelette over and cook for 1 more minute.");
        addIngredient(db, id, "egg", 3, "pcs");
        addIngredient(db, id, "cheese", 40, "g");
        addIngredient(db, id, "butter", 10, "g");

        // 10
        id = addRecipe(db, "Baked Potatoes",
                "1. Heat the oven to 200 degrees.\n"
                        + "2. Cut the potatoes into wedges and toss with the oil and salt.\n"
                        + "3. Bake for 35 to 40 minutes until golden.");
        addIngredient(db, id, "potato", 4, "pcs");
        addIngredient(db, id, "oil", 2, "tbsp");
        addIngredient(db, id, "salt", 1, "tsp");

        // 11
        id = addRecipe(db, "Garlic Bread",
                "1. Crush the garlic and mix it into the soft butter.\n"
                        + "2. Spread the garlic butter on the bread.\n"
                        + "3. Bake at 180 degrees for 10 minutes until crispy.");
        addIngredient(db, id, "bread", 4, "pcs");
        addIngredient(db, id, "butter", 40, "g");
        addIngredient(db, id, "garlic", 2, "pcs");

        // 12
        id = addRecipe(db, "Vegetable Soup",
                "1. Peel and chop the potatoes, carrots and onion.\n"
                        + "2. Fry the onion in the oil for 3 minutes.\n"
                        + "3. Add the other vegetables, the salt and enough water to cover them.\n"
                        + "4. Simmer for 25 minutes, then serve.");
        addIngredient(db, id, "potato", 2, "pcs");
        addIngredient(db, id, "carrot", 2, "pcs");
        addIngredient(db, id, "onion", 1, "pcs");
        addIngredient(db, id, "oil", 1, "tbsp");
        addIngredient(db, id, "salt", 1, "tsp");

        // 13
        id = addRecipe(db, "Peanut Butter Banana Toast",
                "1. Toast the bread.\n"
                        + "2. Spread the peanut butter on both slices.\n"
                        + "3. Slice the banana and put it on top.");
        addIngredient(db, id, "bread", 2, "pcs");
        addIngredient(db, id, "peanut butter", 2, "tbsp");
        addIngredient(db, id, "banana", 1, "pcs");

        // 14
        id = addRecipe(db, "Beans on Toast",
                "1. Heat the beans in a small pot for 5 minutes.\n"
                        + "2. Toast the bread and spread it with the butter.\n"
                        + "3. Pour the beans over the toast.");
        addIngredient(db, id, "bean", 200, "g");
        addIngredient(db, id, "bread", 2, "pcs");
        addIngredient(db, id, "butter", 10, "g");

        // 15
        id = addRecipe(db, "Chicken and Rice",
                "1. Cut the chicken into pieces and season with the salt.\n"
                        + "2. Fry the chopped onion in the oil, then add the chicken until brown.\n"
                        + "3. Add the rice and enough water to cover it.\n"
                        + "4. Cover and cook on low heat for 20 minutes until the rice is soft.");
        addIngredient(db, id, "chicken", 300, "g");
        addIngredient(db, id, "rice", 150, "g");
        addIngredient(db, id, "onion", 1, "pcs");
        addIngredient(db, id, "oil", 1, "tbsp");
        addIngredient(db, id, "salt", 1, "tsp");

        // 16
        id = addRecipe(db, "Cucumber Yoghurt Salad",
                "1. Dice the cucumber.\n"
                        + "2. Mix it with the yoghurt, salt and the juice of the lemon.\n"
                        + "3. Chill for 10 minutes and serve.");
        addIngredient(db, id, "cucumber", 1, "pcs");
        addIngredient(db, id, "yoghurt", 150, "g");
        addIngredient(db, id, "lemon", 1, "pcs");
        addIngredient(db, id, "salt", 1, "tsp");

        // 17
        id = addRecipe(db, "Cinnamon Sugar Toast",
                "1. Toast the bread.\n"
                        + "2. Spread the butter on while it is still warm.\n"
                        + "3. Mix the sugar and cinnamon and sprinkle it on top.");
        addIngredient(db, id, "bread", 2, "pcs");
        addIngredient(db, id, "butter", 20, "g");
        addIngredient(db, id, "sugar", 1, "tbsp");
        addIngredient(db, id, "cinnamon", 1, "tsp");

        // 18
        id = addRecipe(db, "Macaroni Cheese",
                "1. Boil the pasta until soft, then drain.\n"
                        + "2. Melt the butter in a pot, stir in the flour, then slowly add the milk.\n"
                        + "3. Stir until the sauce gets thick, then add the cheese until it melts.\n"
                        + "4. Mix in the pasta and serve.");
        addIngredient(db, id, "pasta", 200, "g");
        addIngredient(db, id, "cheese", 100, "g");
        addIngredient(db, id, "milk", 200, "ml");
        addIngredient(db, id, "butter", 20, "g");
        addIngredient(db, id, "flour", 20, "g");
    }
}