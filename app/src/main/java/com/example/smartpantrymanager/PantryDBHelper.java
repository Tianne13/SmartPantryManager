package com.example.smartpantrymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// This class creates the database and its tables.
// It extends SQLiteOpenHelper, which does most of the work for us.
public class PantryDBHelper extends SQLiteOpenHelper {

    // Name of the database file
    private static final String DATABASE_NAME = "pantry.db";

    // Version number. If I change the tables later, I increase this number.
    private static final int DATABASE_VERSION = 1;

    // SQL command to create the pantry_items table
    private static final String CREATE_TABLE_PANTRY =
            "CREATE TABLE pantry_items ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "quantity REAL, "
                    + "unit TEXT, "
                    + "expiry_date TEXT);";

    // SQL command to create the recipes table
    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE recipes ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "steps TEXT);";

    // SQL command to create the recipe_ingredients table.
    // recipe_id links each ingredient to its recipe.
    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "CREATE TABLE recipe_ingredients ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "recipe_id INTEGER, "
                    + "ingredient_name TEXT NOT NULL, "
                    + "quantity REAL, "
                    + "unit TEXT);";

    // Constructor: it just calls the constructor of the parent class
    public PantryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Runs the first time the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PANTRY);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);
    }

    // Runs when the version number goes up.
    // It deletes the old tables and creates them again.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PantryDBHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS pantry_items");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        onCreate(db);
    }
}