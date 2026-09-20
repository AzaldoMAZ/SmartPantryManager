package com.azaldo.smartpantrymanager.utils;

import android.content.Context;

import com.azaldo.smartpantrymanager.models.PantryItem;
import com.azaldo.smartpantrymanager.models.Recipe;
import com.azaldo.smartpantrymanager.models.RecipeIngredient;
import com.azaldo.smartpantrymanager.repositories.PantryRepository;
import com.azaldo.smartpantrymanager.repositories.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the assignment's core business rule: a recipe is only
 * "suggested" if every one of its required ingredients is present in the
 * pantry, in at least the required quantity. Partial matches are excluded
 * entirely - there is no partial-credit result from this class.
 *
 * Kept deliberately separate from any Activity: the Suggested Recipes
 * screen calls getSuggestedRecipes() and displays whatever comes back,
 * it does not know or care how the decision was made.
 */
public class RecipeMatcher {

    private final PantryRepository pantryRepository;
    private final RecipeRepository recipeRepository;

    public RecipeMatcher(Context context) {
        this.pantryRepository = new PantryRepository(context);
        this.recipeRepository = new RecipeRepository(context);
    }

    /** Returns only the recipes that strictly qualify against the current pantry. */
    public List<Recipe> getSuggestedRecipes() {
        List<Recipe> allRecipes = recipeRepository.getAllRecipes();
        List<Recipe> suggested = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (recipeQualifies(recipe)) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }

    /**
     * A recipe qualifies only if every required ingredient is satisfied.
     * The loop exits (returns false) on the first unmet requirement -
     * there is no "missing one ingredient" partial result here, matching
     * the assignment's strict-matching rule.
     */
    public boolean recipeQualifies(Recipe recipe) {
        List<RecipeIngredient> requirements = recipeRepository.getIngredientsForRecipe(recipe.getId());

        for (RecipeIngredient requirement : requirements) {
            if (!isRequirementSatisfied(requirement)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks one ingredient requirement against the pantry. Sums every
     * pantry item with a matching normalized name, converting each into
     * the requirement's unit where the units are compatible. A pantry
     * item in an incompatible unit (e.g. requirement is in g, pantry item
     * is in pieces) contributes nothing rather than being guessed at.
     */
    private boolean isRequirementSatisfied(RecipeIngredient requirement) {
        List<PantryItem> matchingItems = pantryRepository.getItemsByNormalizedName(requirement.getNormalizedName());

        double totalAvailable = 0;
        for (PantryItem item : matchingItems) {
            Double converted = UnitConverter.convert(item.getQuantity(), item.getUnit(), requirement.getUnit());
            if (converted != null) {
                totalAvailable += converted;
            }
        }

        return totalAvailable >= requirement.getRequiredQuantity();
    }
}
