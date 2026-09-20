package com.azaldo.smartpantrymanager.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azaldo.smartpantrymanager.R;
import com.azaldo.smartpantrymanager.adapters.RecipeAdapter;
import com.azaldo.smartpantrymanager.models.Recipe;
import com.azaldo.smartpantrymanager.utils.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays only the recipes that strictly qualify against the current
 * pantry, via RecipeMatcher. Data is reloaded in onResume so that
 * editing the pantry and returning here always reflects the latest
 * matching result, without needing startActivityForResult.
 */
public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerViewRecipes;
    private TextView textNoRecipes;
    private RecipeMatcher recipeMatcher;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle("Suggested Recipes");

        recipeMatcher = new RecipeMatcher(this);

        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        textNoRecipes = findViewById(R.id.textNoRecipes);

        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(new ArrayList<>(), this);
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSuggestions();
    }

    private void refreshSuggestions() {
        List<Recipe> suggestions = recipeMatcher.getSuggestedRecipes();
        recipeAdapter.updateData(suggestions);

        boolean isEmpty = suggestions.isEmpty();
        textNoRecipes.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerViewRecipes.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        startActivity(RecipeDetailActivity.newIntent(this, recipe.getId()));
    }
}
