package com.azaldo.smartpantrymanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.azaldo.smartpantrymanager.R;
import com.azaldo.smartpantrymanager.models.Recipe;
import com.azaldo.smartpantrymanager.models.RecipeIngredient;
import com.azaldo.smartpantrymanager.repositories.RecipeRepository;

import java.util.List;

/**
 * Shows the full detail for a single recipe. Only the recipe's id is
 * passed through the Intent (never the full recipe data) - the full
 * Recipe and its ingredients are loaded from the database here, per the
 * assignment's guidance against passing large amounts of data through
 * an Intent.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    private RecipeRepository recipeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setTitle("Recipe Detail");

        recipeRepository = new RecipeRepository(this);

        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRecipe(recipeId);
    }

    private void loadRecipe(long recipeId) {
        Recipe recipe = recipeRepository.getRecipeById(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        List<RecipeIngredient> ingredients = recipeRepository.getIngredientsForRecipe(recipeId);

        TextView textDetailName = findViewById(R.id.textDetailName);
        TextView textDetailDescription = findViewById(R.id.textDetailDescription);
        TextView textDetailIngredients = findViewById(R.id.textDetailIngredients);
        TextView textDetailSteps = findViewById(R.id.textDetailSteps);

        textDetailName.setText(recipe.getName());
        textDetailDescription.setText(recipe.getDescription());
        textDetailIngredients.setText(formatIngredients(ingredients));
        textDetailSteps.setText(recipe.getPreparationSteps());
    }

    private String formatIngredients(List<RecipeIngredient> ingredients) {
        StringBuilder builder = new StringBuilder();
        for (RecipeIngredient ingredient : ingredients) {
            String quantityText = ingredient.getRequiredQuantity() == Math.floor(ingredient.getRequiredQuantity())
                    ? String.valueOf((long) ingredient.getRequiredQuantity())
                    : String.valueOf(ingredient.getRequiredQuantity());

            builder.append("- ")
                    .append(quantityText)
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getIngredientName())
                    .append("\n");
        }
        // Trim the trailing newline so the TextView doesn't show an empty last line.
        return builder.toString().trim();
    }

    public static Intent newIntent(Context context, long recipeId) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }
}
