package com.azaldo.smartpantrymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azaldo.smartpantrymanager.R;
import com.azaldo.smartpantrymanager.models.Recipe;

import java.util.List;

/**
 * Binds a List<Recipe> to the Suggested Recipes RecyclerView. Tapping a
 * row is delegated to the hosting Activity via OnRecipeClickListener,
 * which is expected to open the Recipe Detail screen.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClicked(Recipe recipe);
    }

    private final List<Recipe> recipes;
    private final OnRecipeClickListener listener;

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.textRecipeName.setText(recipe.getName());
        holder.textRecipeDescription.setText(recipe.getDescription());
        holder.itemView.setOnClickListener(v -> listener.onRecipeClicked(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateData(List<Recipe> newRecipes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView textRecipeName;
        TextView textRecipeDescription;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textRecipeName = itemView.findViewById(R.id.textRecipeName);
            textRecipeDescription = itemView.findViewById(R.id.textRecipeDescription);
        }
    }
}
