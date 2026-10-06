package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

// This class opens and closes the database and holds the methods
// for adding, reading, updating and deleting pantry items.
public class PantryDataSource {

    private SQLiteDatabase database;
    private PantryDBHelper dbHelper;

    // Constructor: creates the helper
    public PantryDataSource(Context context) {
        dbHelper = new PantryDBHelper(context);
    }

    // Opens the database so we can use it
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Closes the database when we are finished
    public void close() {
        dbHelper.close();
    }

    // CREATE: adds a new pantry item. Returns true if it worked.
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

    // UPDATE: changes an existing pantry item. Returns true if it worked.
    public boolean updatePantryItem(PantryItem item) {
        boolean didSucceed = false;
        try {
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            values.put("quantity", item.getQuantity());
            values.put("unit", item.getUnit());
            values.put("expiry_date", item.getExpiryDate());

            // The id tells the database which row to change
            didSucceed = database.update("pantry_items", values, "_id = ?",
                    new String[]{String.valueOf(item.getId())}) > 0;
        } catch (Exception e) {
            didSucceed = false;
        }
        return didSucceed;
    }

    // DELETE: removes a pantry item. Returns true if it worked.
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

    // READ: gets all pantry items, sorted by name
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

    // READ: gets one pantry item using its id (used by the Edit screen)
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
}