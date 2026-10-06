package com.example.smartpantrymanager;

// This class holds one ingredient that a recipe needs
public class RecipeIngredient {

    private int id;
    private int recipeId; // which recipe this ingredient belongs to
    private String ingredientName;
    private double quantity;
    private String unit;

    // Empty constructor
    public RecipeIngredient() {
        id = -1; // -1 means this ingredient is not in the database yet
    }

    public RecipeIngredient(int id, int recipeId, String ingredientName, double quantity, String unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
