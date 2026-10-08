package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// this screen is used to add a new ingredient AND to edit an existing one
public class AddEditIngredientActivity extends AppCompatActivity {

    TextView tvFormTitle;
    EditText etName;
    EditText etQuantity;
    EditText etExpiry;
    Spinner spUnit;
    Button btnSave;
    Button btnCancel;

    // the units the user can pick from
    String[] units = {"g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"};

    // -1 means I am adding a new item, anything else is the id of the item I am editing
    int currentItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // connecting my variables to the views in the layout
        tvFormTitle = findViewById(R.id.tvFormTitle);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // put the units into the drop-down list
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, units);
        spUnit.setAdapter(unitAdapter);

        // check if the pantry list sent an item id with the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentItemId = extras.getInt("itemId", -1);
        }

        // if there is an id then I am editing, so fill in the form with that item
        if (currentItemId != -1) {
            tvFormTitle.setText("Edit Ingredient");
            loadItemForEditing(currentItemId);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIngredient();
            }
        });

        // cancel just closes this screen and goes back to the list
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // gets the item from the database and shows its details in the form
    private void loadItemForEditing(int id) {
        PantryDataSource dataSource = new PantryDataSource(this);
        try {
            dataSource.open();
            PantryItem item = dataSource.getPantryItem(id);
            dataSource.close();

            etName.setText(item.getName());
            etQuantity.setText(String.valueOf(item.getQuantity()));
            etExpiry.setText(item.getExpiryDate());

            // find the item's unit in my list and select it in the spinner
            for (int i = 0; i < units.length; i++) {
                if (units[i].equals(item.getUnit())) {
                    spUnit.setSelection(i);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Could not load the ingredient", Toast.LENGTH_LONG).show();
        }
    }

    // checks what the user typed, returns true only if everything is ok
    private boolean inputIsValid() {
        String name = etName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // name can't be empty
        if (name.isEmpty()) {
            etName.setError("Please enter an ingredient name");
            return false;
        }

        // quantity can't be empty
        if (quantityText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            return false;
        }

        // quantity must be a number more than 0
        try {
            double quantity = Double.parseDouble(quantityText);
            if (quantity <= 0) {
                etQuantity.setError("Quantity must be more than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Please enter a valid number");
            return false;
        }

        // expiry date is optional but if it is typed it must look like YYYY-MM-DD
        if (!expiry.isEmpty() && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            etExpiry.setError("Use the format YYYY-MM-DD");
            return false;
        }

        return true;
    }

    // saves a new item or updates the one I am editing
    private void saveIngredient() {
        if (!inputIsValid()) {
            return; // error message is already showing so stop here
        }

        // make a PantryItem from what the user typed
        PantryItem item = new PantryItem();
        item.setName(etName.getText().toString().trim());
        item.setQuantity(Double.parseDouble(etQuantity.getText().toString().trim()));
        item.setUnit(spUnit.getSelectedItem().toString());
        item.setExpiryDate(etExpiry.getText().toString().trim());

        PantryDataSource dataSource = new PantryDataSource(this);
        boolean wasSuccessful = false;
        try {
            dataSource.open();
            if (currentItemId == -1) {
                // no id so it is a new item
                wasSuccessful = dataSource.insertPantryItem(item);
            } else {
                // there is an id so update the existing item
                item.setId(currentItemId);
                wasSuccessful = dataSource.updatePantryItem(item);
            }
            dataSource.close();
        } catch (Exception e) {
            wasSuccessful = false;
        }

        if (wasSuccessful) {
            if (currentItemId == -1) {
                Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
            }
            finish(); // go back to the list
        } else {
            Toast.makeText(this, "Could not save the ingredient", Toast.LENGTH_LONG).show();
        }
    }
}