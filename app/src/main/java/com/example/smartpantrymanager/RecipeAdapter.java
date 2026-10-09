package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// adapter that shows each suggested recipe as a row in the list
public class RecipeAdapter extends RecyclerView.Adapter {

    // the recipes to show
    private ArrayList<Recipe> recipeData;

    // the activity gives me this so it can decide what happens when a row is tapped
    private View.OnClickListener onItemClickListener;

    // holds the views for one row
    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);

            // tag lets the activity find out which row was tapped
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }

    // gives the adapter the list to display
    public RecipeAdapter(ArrayList<Recipe> arrayList) {
        recipeData = arrayList;
    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        onItemClickListener = listener;
    }

    // makes a new row from the item_recipe layout
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(v);
    }

    // puts the recipe name in the row
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeViewHolder rvh = (RecipeViewHolder) holder;
        rvh.tvName.setText(recipeData.get(position).getName());
    }

    // how many rows there are
    @Override
    public int getItemCount() {
        return recipeData.size();
    }
}