package com.example.smartpantrymanager;

import java.util.ArrayList;

// this class has the strict matching logic of my app
// a recipe is only suggested if the pantry has EVERY ingredient it needs,
// in at least the amount the recipe needs
public class RecipeMatcher {

    // makes names easier to compare: lowercase, no extra spaces and singular
    // so "Tomatoes " and "tomato" end up being the same word
    public static String normalizeName(String name) {
        if (name == null) {
            return "";
        }

        String n = name.trim().toLowerCase();
        n = n.replaceAll("\\s+", " "); // turn double spaces into one space

        if (n.endsWith("ies") && n.length() > 3) {
            // berries -> berry
            n = n.substring(0, n.length() - 3) + "y";
        } else if (n.endsWith("oes")) {
            // tomatoes -> tomato, potatoes -> potato
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("ches") || n.endsWith("shes")
                || n.endsWith("xes") || n.endsWith("sses")) {
            // peaches -> peach
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("s") && !n.endsWith("ss")) {
            // eggs -> egg, onions -> onion
            n = n.substring(0, n.length() - 1);
        }
        return n;
    }

    // tells me what kind of unit it is (weight, volume or count)
    // I can only compare units that are the same kind
    public static String getUnitType(String unit) {
        if (unit == null) {
            return "other";
        }

        String u = unit.trim().toLowerCase();

        if (u.equals("g") || u.equals("kg")) {
            return "weight";
        }
        if (u.equals("ml") || u.equals("l") || u.equals("tsp")
                || u.equals("tbsp") || u.equals("cup")) {
            return "volume";
        }
        if (u.equals("pcs")) {
            return "count";
        }
        return "other";
    }

    // changes a quantity into the base unit: grams for weight, ml for volume
    // example: 2 kg becomes 2000, 1 tbsp becomes 15
    public static double toBaseAmount(double quantity, String unit) {
        if (unit == null) {
            return quantity;
        }

        String u = unit.trim().toLowerCase();

        if (u.equals("kg")) {
            return quantity * 1000;
        }
        if (u.equals("l")) {
            return quantity * 1000;
        }
        if (u.equals("tsp")) {
            return quantity * 5;
        }
        if (u.equals("tbsp")) {
            return quantity * 15;
        }
        if (u.equals("cup")) {
            return quantity * 250;
        }
        // g, ml and pcs stay the same
        return quantity;
    }

    // checks if the pantry has enough of ONE ingredient that a recipe needs
    public static boolean pantryHasIngredient(ArrayList<PantryItem> pantry,
                                              RecipeIngredient needed) {
        String neededName = normalizeName(needed.getIngredientName());
        String neededType = getUnitType(needed.getUnit());
        double neededAmount = toBaseAmount(needed.getQuantity(), needed.getUnit());

        // total I have of this ingredient in the same kind of unit
        double totalSameType = 0;
        // true if I have it but in a unit I can't compare (like tsp vs g)
        boolean foundOtherType = false;

        for (PantryItem item : pantry) {
            // only look at pantry items with the same name
            if (normalizeName(item.getName()).equals(neededName)) {
                if (getUnitType(item.getUnit()).equals(neededType)) {
                    // same kind of unit so I can add up the amounts
                    totalSameType = totalSameType
                            + toBaseAmount(item.getQuantity(), item.getUnit());
                } else if (item.getQuantity() > 0) {
                    foundOtherType = true;
                }
            }
        }

        // enough in the same kind of unit
        if (totalSameType >= neededAmount) {
            return true;
        }

        // I have none in a comparable unit but I do have some in another unit
        // I can't compare them so I just accept that the ingredient is there
        if (totalSameType == 0 && foundOtherType) {
            return true;
        }

        // not enough or not there at all
        return false;
    }

    // THE STRICT RULE: every single ingredient must be in the pantry
    public static boolean canMakeRecipe(ArrayList<RecipeIngredient> neededIngredients,
                                        ArrayList<PantryItem> pantry) {
        // a recipe with no ingredients should never be suggested
        if (neededIngredients.size() == 0) {
            return false;
        }

        for (RecipeIngredient ingredient : neededIngredients) {
            if (!pantryHasIngredient(pantry, ingredient)) {
                // one missing ingredient is enough to reject the whole recipe
                return false;
            }
        }

        // nothing was missing
        return true;
    }

    // goes through all the recipes and gives back only the ones I can make now
    // the data source must already be open when this is called
    public static ArrayList<Recipe> getSuggestedRecipes(PantryDataSource dataSource) {
        ArrayList<Recipe> suggested = new ArrayList<Recipe>();

        ArrayList<PantryItem> pantry = dataSource.getAllPantryItems();
        ArrayList<Recipe> allRecipes = dataSource.getAllRecipes();

        for (Recipe recipe : allRecipes) {
            ArrayList<RecipeIngredient> needed =
                    dataSource.getIngredientsForRecipe(recipe.getId());

            if (canMakeRecipe(needed, pantry)) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }
}