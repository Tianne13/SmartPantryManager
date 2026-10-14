package com.example.smartpantrymanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// adapter that shows each pantry item as a row in the list
public class PantryAdapter extends RecyclerView.Adapter {

    // the items to show
    private ArrayList<PantryItem> pantryData;

    // these are given to me by MainActivity so it can decide what happens on a click
    private View.OnClickListener onItemClickListener;
    private View.OnClickListener onDeleteClickListener;

    // true if the user turned on the expiring-soon alerts in Settings
    private boolean expiryAlertsOn = false;

    // holds the views for one row
    public class PantryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvQuantity;
        public TextView tvExpiry;
        public Button btnDelete;

        // I save the normal text colour so I can put it back after a red warning
        public int defaultExpiryColor;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
            defaultExpiryColor = tvExpiry.getCurrentTextColor();

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

    // MainActivity tells me if the expiry alerts are on or off
    public void setExpiryAlertsOn(boolean alertsOn) {
        expiryAlertsOn = alertsOn;
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

        // start with the normal text and colour (rows get reused so I have to reset them)
        pvh.tvExpiry.setTextColor(pvh.defaultExpiryColor);

        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {
            // expiry date is optional so there might not be one
            pvh.tvExpiry.setText("No expiry date");
        } else {
            pvh.tvExpiry.setText("Expires: " + item.getExpiryDate());

            // only show the warning if the setting is on
            if (expiryAlertsOn) {
                int daysLeft = daysUntilExpiry(item.getExpiryDate());

                if (daysLeft < 0) {
                    pvh.tvExpiry.setText("EXPIRED on " + item.getExpiryDate());
                    pvh.tvExpiry.setTextColor(Color.RED);
                } else if (daysLeft == 0) {
                    pvh.tvExpiry.setText("Expiring soon! Expires today");
                    pvh.tvExpiry.setTextColor(Color.RED);
                } else if (daysLeft <= 3) {
                    pvh.tvExpiry.setText("Expiring soon! Expires in " + daysLeft
                            + " day(s) (" + item.getExpiryDate() + ")");
                    pvh.tvExpiry.setTextColor(Color.RED);
                }
            }
        }
    }

    // works out how many days are left until the expiry date
    // negative means it is already expired, 9999 means the date could not be read
    private int daysUntilExpiry(String expiryDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            format.setLenient(false);

            Date expiry = format.parse(expiryDate);

            // today's date without the time (I format it and read it back)
            Date today = format.parse(format.format(new Date()));

            long difference = expiry.getTime() - today.getTime();
            double oneDay = 1000 * 60 * 60 * 24;
            return (int) Math.round(difference / oneDay);
        } catch (Exception e) {
            // the date was not a real date so I just don't show a warning
            return 9999;
        }
    }

    // how many rows there are
    @Override
    public int getItemCount() {
        return pantryData.size();
    }
}