package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter {

    private ArrayList<PantryItem> pantryData;

    public class PantryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvQuantity;
        public TextView tvExpiry;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
        }
    }

    public PantryAdapter(ArrayList<PantryItem> arrayList) {
        pantryData = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PantryViewHolder pvh = (PantryViewHolder) holder;
        PantryItem item = pantryData.get(position);

        pvh.tvName.setText(item.getName());
        pvh.tvQuantity.setText("Quantity: " + item.getQuantity() + " " + item.getUnit());

        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {
            pvh.tvExpiry.setText("No expiry date");
        } else {
            pvh.tvExpiry.setText("Expires: " + item.getExpiryDate());
        }
    }

    @Override
    public int getItemCount() {
        return pantryData.size();
    }
}