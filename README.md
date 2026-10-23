# SMART PANTRY MANAGER

An Android app written in Java that helps reduce food waste. The user keeps track of the ingredients they already have at home (their "pantry") and the app suggests recipes they can cook **using only what they already have**. A recipe is only suggested if the pantry has **every** ingredient it needs, in at least the required quantity.

Built for the **Mobile App Development 700** practical assignment at Richfield Graduate Institute of Technology.

- **Student:** Tianne Willemse
- **GitHub:** https://github.com/Tianne13/SmartPantryManager

---

## FEATURES

- **Pantry Management:** add, edit and delete pantry items (name, quantity, unit and an optional expiry date).
- **Pantry List:** all ingredients shown in a RecyclerView with a custom adapter, loaded from the database.
- **Strict Recipe Matching:** the Suggested Recipes screen only lists recipes where *every* ingredient is in the pantry in at least the required amount. A recipe with one missing ingredient is not shown.
- **Smart Matching:** names are cleaned up so "Tomato" and "tomatoes" match, and units are converted so "0.01 kg" and "10 g" are treated as the same amount.
- **18 Starter Recipes** loaded into the database the first time the app runs, each with ingredients and preparation steps.
- **Recipe Detail Screen:** full ingredient list and method for a selected recipe.
- **Settings Screen:** a toggle for expiring-soon alerts (saved with SharedPreferences). When it is on, pantry items that expire within 3 days, or have already expired, are shown in red.
- **Friendly Empty States:** messages such as "No recipes match your pantry yet - add more ingredients" instead of a blank screen.
- **Input Validation** on the add/edit form (name required, quantity must be a number above 0, expiry date must be in the format YYYY-MM-DD).
- **Bottom Navigation Bar** to move between Pantry, Recipes and Settings.

The app does **not** use Google Maps, any mapping SDK, or GPS/location services.

## DATABASE CHOICE: SQLite

I chose **SQLite** (using `SQLiteOpenHelper`) because:

- it is stored locally on the phone, so the app works without internet or a separate server;
- it is the approach covered in the module's persistent data chapter;
- a personal pantry app only needs a small amount of structured data, which suits a local database;
- the data stays on the device after the app is closed and reopened.

### TABLES

| Table | Columns |
|---|---|
| `pantry_items` | `_id`, `name`, `quantity`, `unit`, `expiry_date` |
| `recipes` | `_id`, `name`, `steps` |
| `recipe_ingredients` | `_id`, `recipe_id`, `ingredient_name`, `quantity`, `unit` |

`recipe_ingredients.recipe_id` links each ingredient to its recipe (one recipe has many ingredients).

## HOW THE STRICT MATCHING WORKS

The logic is in `RecipeMatcher.java`:

1. Names are normalised (lowercase, trimmed, plurals turned into singular).
2. Units are converted to a base unit (kg to g, and l, tsp, tbsp and cup to ml).
3. For each ingredient a recipe needs, the amounts of all matching pantry rows are added up and compared with the amount required.
4. If even **one** ingredient is missing or not enough, the whole recipe is rejected.

## PROJECT STRUCTURE

| File | Purpose |
|---|---|
| `MainActivity` | Pantry list screen, delete confirmation, opens the edit form |
| `AddEditIngredientActivity` | Form to add or edit an ingredient, with validation |
| `SuggestedRecipesActivity` | Shows only the recipes that pass the strict matching |
| `RecipeDetailActivity` | Shows one recipe's ingredients and method |
| `SettingsActivity` | Expiring-soon alerts toggle (SharedPreferences) |
| `PantryAdapter`, `RecipeAdapter` | RecyclerView adapters for the two lists |
| `PantryDBHelper` | Creates the SQLite database and tables, seeds the recipes |
| `PantryDataSource` | Opens and closes the database, and holds the CRUD and read methods |
| `RecipeMatcher` | The strict-matching logic |
| `PantryItem`, `Recipe`, `RecipeIngredient` | Model classes |

## SETUP & RUN INSTRUCTIONS

**Requirements**

- Android Studio (a recent version)
- An Android emulator (or a physical Android phone) running Android 7.0 (API 24) or higher

**Steps**

1. Clone the repository:
   ```
   git clone https://github.com/Tianne13/SmartPantryManager.git
   ```
   or download it as a ZIP from GitHub and extract it.
2. Open Android Studio and choose **File > Open**, then select the `SmartPantryManager` folder.
3. Wait for Gradle to finish syncing (the progress bar at the bottom).
4. Start an emulator in **Device Manager**, or connect a phone with USB debugging turned on.
5. Wait until the emulator shows its home screen, then click the green **Run** button.

The recipes are added to the database automatically the first time the app runs. No other setup is needed.

## HOW TO USE THE APP

1. On the **Pantry** screen, tap **Add Ingredient** and enter what you have at home.
2. Tap a row to edit an ingredient, or tap **Delete** to remove it.
3. Tap **Recipes** in the navigation bar to see the recipes you can make right now.
4. Tap a recipe to see its ingredients and method.
5. Open **Settings** to turn expiring-soon alerts on or off.

## KNOWN LIMITATIONS

- Weight units (g, kg), volume units (ml, l, tsp, tbsp, cup) and counts (pcs) cannot be converted into each other without extra data such as ingredient densities. When the units are of different kinds, the app only checks that the ingredient is present.
- Plural handling covers common English patterns only.
- The expiry date is typed in as text (YYYY-MM-DD) and could be replaced with a date picker.

## POSSIBLE FUTURE IMPROVEMENTS

- An "AlmostThere" list of recipes that are missing only one ingredient.
- A date picker for expiry dates.
- Users adding their own recipes.