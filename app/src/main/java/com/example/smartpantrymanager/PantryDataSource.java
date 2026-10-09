package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

// this class opens and closes the database and has the methods
// for adding, reading, updating and deleting my data
public class PantryDataSource {

    private SQLiteDatabase database;
    private PantryDBHelper dbHelper;

    // creates the helper
    public PantryDataSource(Context context) {
        dbHelper = new PantryDBHelper(context);
    }

    // opens the database so I can use it
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // closes the database when I am done
    public void close() {
        dbHelper.close();
    }

    // ---------- PANTRY ITEMS ----------

    // CREATE: adds a new pantry item, gives back true if it worked
    public boolean insertPantryItem(PantryItem item) {
        boolean didSucceed = false;
        try {
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            values.put("quantity", item.getQuantity());
            values.put("unit", item.getUnit());
            values.put("expiry_date", item.getExpiryDate());

            didSucceed = database.insert("pantry_items", null, values) > 0;
        } catch (Exception e) {
            didSucceed = false;
        }
        return didSucceed;
    }

    // UPDATE: changes an existing pantry item, gives back true if it worked
    public boolean updatePantryItem(PantryItem item) {
        boolean didSucceed = false;
        try {
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            values.put("quantity", item.getQuantity());
            values.put("unit", item.getUnit());
            values.put("expiry_date", item.getExpiryDate());

            // the id tells the database which row to change
            didSucceed = database.update("pantry_items", values, "_id = ?",
                    new String[]{String.valueOf(item.getId())}) > 0;
        } catch (Exception e) {
            didSucceed = false;
        }
        return didSucceed;
    }

    // DELETE: removes a pantry item, gives back true if it worked
    public boolean deletePantryItem(int id) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("pantry_items", "_id = ?",
                    new String[]{String.valueOf(id)}) > 0;
        } catch (Exception e) {
            didDelete = false;
        }
        return didDelete;
    }

    // READ: gets all the pantry items, sorted by name
    public ArrayList<PantryItem> getAllPantryItems() {
        ArrayList<PantryItem> items = new ArrayList<PantryItem>();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM pantry_items ORDER BY name", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                PantryItem item = new PantryItem();
                item.setId(cursor.getInt(0));            // _id
                item.setName(cursor.getString(1));       // name
                item.setQuantity(cursor.getDouble(2));   // quantity
                item.setUnit(cursor.getString(3));       // unit
                item.setExpiryDate(cursor.getString(4)); // expiry_date
                items.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            items = new ArrayList<PantryItem>();
        }
        return items;
    }

    // READ: gets one pantry item by its id (used by the edit screen)
    public PantryItem getPantryItem(int id) {
        PantryItem item = new PantryItem();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM pantry_items WHERE _id = ?",
                    new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {
                item.setId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setQuantity(cursor.getDouble(2));
                item.setUnit(cursor.getString(3));
                item.setExpiryDate(cursor.getString(4));
            }
            cursor.close();
        } catch (Exception e) {
            item = new PantryItem();
        }
        return item;
    }

    // ---------- RECIPES ----------

    // READ: gets all the recipes, sorted by name
    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM recipes ORDER BY name", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(0));        // _id
                recipe.setName(cursor.getString(1));   // name
                recipe.setSteps(cursor.getString(2));  // steps
                recipes.add(recipe);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            recipes = new ArrayList<Recipe>();
        }
        return recipes;
    }

    // READ: gets one recipe by its id (used by the recipe detail screen)
    public Recipe getRecipe(int id) {
        Recipe recipe = new Recipe();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM recipes WHERE _id = ?",
                    new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {
                recipe.setId(cursor.getInt(0));
                recipe.setName(cursor.getString(1));
                recipe.setSteps(cursor.getString(2));
            }
            cursor.close();
        } catch (Exception e) {
            recipe = new Recipe();
        }
        return recipe;
    }

    // READ: gets all the ingredients that one recipe needs
    public ArrayList<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();
        try {
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM recipe_ingredients WHERE recipe_id = ?",
                    new String[]{String.valueOf(recipeId)});

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RecipeIngredient ingredient = new RecipeIngredient();
                ingredient.setId(cursor.getInt(0));                // _id
                ingredient.setRecipeId(cursor.getInt(1));          // recipe_id
                ingredient.setIngredientName(cursor.getString(2)); // ingredient_name
                ingredient.setQuantity(cursor.getDouble(3));       // quantity
                ingredient.setUnit(cursor.getString(4));           // unit
                ingredients.add(ingredient);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            ingredients = new ArrayList<RecipeIngredient>();
        }
        return ingredients;
    }
}