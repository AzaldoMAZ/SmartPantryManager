package com.azaldo.smartpantrymanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Central SQLiteOpenHelper for the app. Owns database creation and
 * upgrades only - actual CRUD logic belongs in the repository classes
 * (PantryRepository, RecipeRepository), not here. Keeping this class
 * limited to schema management makes it easy to explain in the video
 * demonstration and keeps repositories independently testable.
 *
 * Four tables, matching the assignment's suggested design:
 *  - PantryItems:        ingredients the user currently has
 *  - Recipes:            recipe name/description/steps
 *  - RecipeIngredients:  the ingredient requirements for each recipe
 *  - Settings:           a single-row table for app preferences
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry_manager.db";
    private static final int DATABASE_VERSION = 1;

    // ---- PantryItems table ----
    public static final String TABLE_PANTRY_ITEMS = "PantryItems";
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_NORMALIZED_NAME = "normalizedName";
    public static final String COLUMN_PANTRY_QUANTITY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY_DATE = "expiryDate";
    public static final String COLUMN_PANTRY_CREATED_AT = "createdAt";

    // ---- Recipes table ----
    public static final String TABLE_RECIPES = "Recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_DESCRIPTION = "description";
    public static final String COLUMN_RECIPE_PREPARATION_STEPS = "preparationSteps";
    public static final String COLUMN_RECIPE_IMAGE_RESOURCE = "imageResourceName";

    // ---- RecipeIngredients table ----
    public static final String TABLE_RECIPE_INGREDIENTS = "RecipeIngredients";
    public static final String COLUMN_RI_ID = "id";
    public static final String COLUMN_RI_RECIPE_ID = "recipeId";
    public static final String COLUMN_RI_INGREDIENT_NAME = "ingredientName";
    public static final String COLUMN_RI_NORMALIZED_NAME = "normalizedName";
    public static final String COLUMN_RI_REQUIRED_QUANTITY = "requiredQuantity";
    public static final String COLUMN_RI_UNIT = "unit";

    // ---- Settings table (single row, id is always 1) ----
    public static final String TABLE_SETTINGS = "Settings";
    public static final String COLUMN_SETTINGS_ID = "id";
    public static final String COLUMN_SETTINGS_EXPIRY_ALERTS_ENABLED = "expiryAlertsEnabled";
    public static final String COLUMN_SETTINGS_PREFERRED_UNIT = "preferredUnit";

    private static DatabaseHelper instance;

    /**
     * Simple singleton so the whole app shares one SQLiteOpenHelper
     * instance rather than opening the database repeatedly.
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY_ITEMS + " ("
                + COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PANTRY_NAME + " TEXT NOT NULL, "
                + COLUMN_PANTRY_NORMALIZED_NAME + " TEXT NOT NULL, "
                + COLUMN_PANTRY_QUANTITY + " REAL NOT NULL, "
                + COLUMN_PANTRY_UNIT + " TEXT NOT NULL, "
                + COLUMN_PANTRY_EXPIRY_DATE + " TEXT, "
                + COLUMN_PANTRY_CREATED_AT + " INTEGER NOT NULL"
                + ");");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " ("
                + COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RECIPE_NAME + " TEXT NOT NULL, "
                + COLUMN_RECIPE_DESCRIPTION + " TEXT, "
                + COLUMN_RECIPE_PREPARATION_STEPS + " TEXT NOT NULL, "
                + COLUMN_RECIPE_IMAGE_RESOURCE + " TEXT"
                + ");");

        // ON DELETE CASCADE means deleting a recipe cleans up its ingredient
        // rows automatically - this must be paired with
        // db.execSQL("PRAGMA foreign_keys = ON") wherever the DB is opened
        // for writes if we rely on it (added when the repository is built).
        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " ("
                + COLUMN_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RI_RECIPE_ID + " INTEGER NOT NULL, "
                + COLUMN_RI_INGREDIENT_NAME + " TEXT NOT NULL, "
                + COLUMN_RI_NORMALIZED_NAME + " TEXT NOT NULL, "
                + COLUMN_RI_REQUIRED_QUANTITY + " REAL NOT NULL, "
                + COLUMN_RI_UNIT + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_RI_RECIPE_ID + ") REFERENCES "
                + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + ") ON DELETE CASCADE"
                + ");");

        db.execSQL("CREATE TABLE " + TABLE_SETTINGS + " ("
                + COLUMN_SETTINGS_ID + " INTEGER PRIMARY KEY CHECK (" + COLUMN_SETTINGS_ID + " = 1), "
                + COLUMN_SETTINGS_EXPIRY_ALERTS_ENABLED + " INTEGER NOT NULL DEFAULT 1, "
                + COLUMN_SETTINGS_PREFERRED_UNIT + " TEXT NOT NULL DEFAULT 'g'"
                + ");");

        // Seed the single settings row so the Settings screen always has
        // something to read/update, instead of having to handle a missing row.
        db.execSQL("INSERT INTO " + TABLE_SETTINGS + " ("
                + COLUMN_SETTINGS_ID + ", " + COLUMN_SETTINGS_EXPIRY_ALERTS_ENABLED + ", "
                + COLUMN_SETTINGS_PREFERRED_UNIT + ") VALUES (1, 1, 'g');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple destructive upgrade strategy - acceptable for a student
        // project with no released data to preserve across schema changes.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
