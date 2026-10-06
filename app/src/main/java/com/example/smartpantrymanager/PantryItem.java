package com.example.smartpantrymanager;

// This class holds the details of one ingredient in the user's pantry
public class PantryItem {

    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate; // optional, can be empty

    // Empty constructor
    public PantryItem() {
        id = -1; // -1 means this item is not in the database yet
    }

    // Constructor used when we already have all the details
    public PantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}