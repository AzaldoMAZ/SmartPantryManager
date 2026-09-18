package com.azaldo.smartpantrymanager.models;

/**
 * Represents one required ingredient line for a recipe, e.g.
 * "500 g chicken" for the recipe with id = recipeId.
 *
 * This is intentionally a separate table/model from PantryItem: a pantry
 * item is something the user owns; a RecipeIngredient is a requirement a
 * recipe has. The RecipeMatcher (added later) compares the two.
 */
public class RecipeIngredient {

    private long id;
    private long recipeId;
    private String ingredientName;
    private String normalizedName;
    private double requiredQuantity;
    private String unit;

    public RecipeIngredient() {
    }

    public RecipeIngredient(long recipeId, String ingredientName, String normalizedName,
                             double requiredQuantity, String unit) {
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.normalizedName = normalizedName;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public double getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
