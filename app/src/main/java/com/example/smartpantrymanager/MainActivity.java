package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvPantry;
    TextView tvEmptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvPantry = findViewById(R.id.rvPantry);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryList();
    }

    private void loadPantryList() {
        PantryDataSource dataSource = new PantryDataSource(this);
        ArrayList<PantryItem> pantryItems;

        try {
            dataSource.open();
            pantryItems = dataSource.getAllPantryItems();
            dataSource.close();

            if (pantryItems.size() == 0) {
                tvEmptyMessage.setVisibility(View.VISIBLE);
                rvPantry.setVisibility(View.GONE);
            } else {
                tvEmptyMessage.setVisibility(View.GONE);
                rvPantry.setVisibility(View.VISIBLE);

                rvPantry.setLayoutManager(new LinearLayoutManager(this));
                PantryAdapter adapter = new PantryAdapter(pantryItems);
                rvPantry.setAdapter(adapter);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving pantry items", Toast.LENGTH_LONG).show();
        }
    }
}