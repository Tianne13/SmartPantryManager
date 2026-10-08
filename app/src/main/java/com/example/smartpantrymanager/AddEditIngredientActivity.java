package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEditIngredientActivity extends AppCompatActivity {

    EditText etName;
    EditText etQuantity;
    EditText etExpiry;
    Spinner spUnit;
    Button btnSave;
    Button btnCancel;

    String[] units = {"g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"};

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

        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, units);
        spUnit.setAdapter(unitAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIngredient();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean inputIsValid() {
        String name = etName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // The name must not be empty
        if (name.isEmpty()) {
            etName.setError("Please enter an ingredient name");
            return false;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            return false;
        }

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

        if (!expiry.isEmpty() && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            etExpiry.setError("Use the format YYYY-MM-DD");
            return false;
        }

        return true;
    }

    private void saveIngredient() {
        if (!inputIsValid()) {
            return;
        }

        PantryItem item = new PantryItem();
        item.setName(etName.getText().toString().trim());
        item.setQuantity(Double.parseDouble(etQuantity.getText().toString().trim()));
        item.setUnit(spUnit.getSelectedItem().toString());
        item.setExpiryDate(etExpiry.getText().toString().trim());

        PantryDataSource dataSource = new PantryDataSource(this);
        boolean wasSuccessful = false;
        try {
            dataSource.open();
            wasSuccessful = dataSource.insertPantryItem(item);
            dataSource.close();
        } catch (Exception e) {
            wasSuccessful = false;
        }

        if (wasSuccessful) {
            Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Could not save the ingredient", Toast.LENGTH_LONG).show();
        }
    }
}