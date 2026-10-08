package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// adapter that shows each pantry item as a row in the list
public class PantryAdapter extends RecyclerView.Adapter {

    // the items to show
    private ArrayList<PantryItem> pantryData;

    // these are given to me by MainActivity so it can decide what happens on a click
    private View.OnClickListener onItemClickListener;
    private View.OnClickListener onDeleteClickListener;

    // holds the views for one row
    public class PantryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvQuantity;
        public TextView tvExpiry;
        public Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);

            // I set the tag so the activity can find out which row was clicked
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
            btnDelete.setTag(this);
            btnDelete.setOnClickListener(onDeleteClickListener);
        }
    }

    // gives the adapter the list to display
    public PantryAdapter(ArrayList<PantryItem> arrayList) {
        pantryData = arrayList;
    }

    // MainActivity uses these to pass in what should happen on a click
    public void setOnItemClickListener(View.OnClickListener listener) {
        onItemClickListener = listener;
    }

    public void setOnDeleteClickListener(View.OnClickListener listener) {
        onDeleteClickListener = listener;
    }

    // makes a new row from the item_pantry layout
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(v);
    }

    // fills a row with the details of one item
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PantryViewHolder pvh = (PantryViewHolder) holder;
        PantryItem item = pantryData.get(position);

        pvh.tvName.setText(item.getName());
        pvh.tvQuantity.setText("Quantity: " + item.getQuantity() + " " + item.getUnit());

        // expiry date is optional so check if it is empty first
        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {
            pvh.tvExpiry.setText("No expiry date");
        } else {
            pvh.tvExpiry.setText("Expires: " + item.getExpiryDate());
        }
    }

    // how many rows there are
    @Override
    public int getItemCount() {
        return pantryData.size();
    }
}