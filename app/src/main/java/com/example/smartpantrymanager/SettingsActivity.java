package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// settings screen, the expiry alert setting is saved with SharedPreferences
public class SettingsActivity extends AppCompatActivity {

    // the name of my settings file and the key for the expiry setting
    // they are public so other screens can read the same setting
    public static final String PREFS_NAME = "PantrySettings";
    public static final String KEY_EXPIRY_ALERTS = "expiryAlerts";

    ToggleButton tbExpiryAlerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // connecting my variable to the toggle in the layout
        tbExpiryAlerts = findViewById(R.id.tbExpiryAlerts);

        initNavigationBar();

        // read the saved setting, false is the default if nothing was saved yet
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean alertsOn = prefs.getBoolean(KEY_EXPIRY_ALERTS, false);
        tbExpiryAlerts.setChecked(alertsOn);

        // when the toggle is pressed, save the new value
        tbExpiryAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpirySetting(tbExpiryAlerts.isChecked());
            }
        });
    }

    // saves the setting so it is still there after the app is closed
    private void saveExpirySetting(boolean isOn) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_EXPIRY_ALERTS, isOn);
        editor.commit();

        if (isOn) {
            Toast.makeText(this, "Expiring-soon alerts turned on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Expiring-soon alerts turned off", Toast.LENGTH_SHORT).show();
        }
    }

    // the navigation bar at the bottom of the screen
    private void initNavigationBar() {
        Button btnNavPantry = findViewById(R.id.btnNavPantry);
        Button btnNavRecipes = findViewById(R.id.btnNavRecipes);
        Button btnNavSettings = findViewById(R.id.btnNavSettings);

        // I am already on this screen so this button is switched off
        btnNavSettings.setEnabled(false);

        btnNavPantry.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            // clear top stops android from making lots of copies of the same screen
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnNavRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, SuggestedRecipesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}