package com.example.smartpantrymanager;

// This class holds one recipe: its name and the preparation steps
public class Recipe {

    private int id;
    private String name;
    private String steps;

    // Empty constructor
    public Recipe() {
        id = -1; // -1 means this recipe is not in the database yet
    }

    public Recipe(int id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }
}