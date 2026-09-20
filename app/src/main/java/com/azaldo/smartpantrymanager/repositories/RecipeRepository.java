package com.azaldo.smartpantrymanager.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.azaldo.smartpantrymanager.database.DatabaseHelper;
import com.azaldo.smartpantrymanager.models.Recipe;
import com.azaldo.smartpantrymanager.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles reads/writes for Recipes and their RecipeIngredients together.
 * A Recipe row without its ingredient rows is not useful on its own, so
 * this repository always loads/saves them as a pair rather than exposing
 * two unrelated CRUD surfaces.
 */
public class RecipeRepository {

    private final DatabaseHelper databaseHelper;

    public RecipeRepository(Context context) {
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Inserts a recipe and all of its ingredient rows in a single
     * transaction, so a crash partway through never leaves a recipe with
     * only some of its ingredients saved.
     *
     * @return the new recipe's id.
     */
    public long addRecipeWithIngredients(Recipe recipe, List<RecipeIngredient> ingredients) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        long recipeId;
        try {
            ContentValues recipeValues = new ContentValues();
            recipeValues.put(DatabaseHelper.COLUMN_RECIPE_NAME, recipe.getName());
            recipeValues.put(DatabaseHelper.COLUMN_RECIPE_DESCRIPTION, recipe.getDescription());
            recipeValues.put(DatabaseHelper.COLUMN_RECIPE_PREPARATION_STEPS, recipe.getPreparationSteps());
            recipeValues.put(DatabaseHelper.COLUMN_RECIPE_IMAGE_RESOURCE, recipe.getImageResourceName());

            recipeId = db.insert(DatabaseHelper.TABLE_RECIPES, null, recipeValues);

            for (RecipeIngredient ingredient : ingredients) {
                ContentValues ingredientValues = new ContentValues();
                ingredientValues.put(DatabaseHelper.COLUMN_RI_RECIPE_ID, recipeId);
                ingredientValues.put(DatabaseHelper.COLUMN_RI_INGREDIENT_NAME, ingredient.getIngredientName());
                ingredientValues.put(DatabaseHelper.COLUMN_RI_NORMALIZED_NAME, ingredient.getNormalizedName());
                ingredientValues.put(DatabaseHelper.COLUMN_RI_REQUIRED_QUANTITY, ingredient.getRequiredQuantity());
                ingredientValues.put(DatabaseHelper.COLUMN_RI_UNIT, ingredient.getUnit());
                db.insert(DatabaseHelper.TABLE_RECIPE_INGREDIENTS, null, ingredientValues);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return recipeId;
    }

    /** Returns every recipe, without their ingredients (use getIngredientsForRecipe for those). */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPES,
                null, null, null, null, null,
                DatabaseHelper.COLUMN_RECIPE_NAME + " ASC"
        );

        while (cursor.moveToNext()) {
            recipes.add(cursorToRecipe(cursor));
        }
        cursor.close();
        return recipes;
    }

    /** Returns a single recipe by id, or null if it doesn't exist. Used by the Recipe Detail screen. */
    public Recipe getRecipeById(long recipeId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPES,
                null,
                DatabaseHelper.COLUMN_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null
        );

        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            recipe = cursorToRecipe(cursor);
        }
        cursor.close();
        return recipe;
    }

    /** Returns every ingredient requirement for a given recipe. Used by RecipeMatcher and Recipe Detail. */
    public List<RecipeIngredient> getIngredientsForRecipe(long recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                null,
                DatabaseHelper.COLUMN_RI_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null
        );

        while (cursor.moveToNext()) {
            ingredients.add(cursorToRecipeIngredient(cursor));
        }
        cursor.close();
        return ingredients;
    }

    /** True if at least one recipe row already exists, used to guard the first-run seeding. */
    public boolean hasAnyRecipes() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_RECIPES, null);
        boolean hasAny = false;
        if (cursor.moveToFirst()) {
            hasAny = cursor.getInt(0) > 0;
        }
        cursor.close();
        return hasAny;
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECIPE_ID)));
        recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECIPE_NAME)));
        recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECIPE_DESCRIPTION)));
        recipe.setPreparationSteps(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECIPE_PREPARATION_STEPS)));
        recipe.setImageResourceName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECIPE_IMAGE_RESOURCE)));
        return recipe;
    }

    private RecipeIngredient cursorToRecipeIngredient(Cursor cursor) {
        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RI_ID)));
        ingredient.setRecipeId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RI_RECIPE_ID)));
        ingredient.setIngredientName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RI_INGREDIENT_NAME)));
        ingredient.setNormalizedName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RI_NORMALIZED_NAME)));
        ingredient.setRequiredQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RI_REQUIRED_QUANTITY)));
        ingredient.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RI_UNIT)));
        return ingredient;
    }
}
