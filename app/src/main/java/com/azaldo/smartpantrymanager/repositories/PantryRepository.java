package com.azaldo.smartpantrymanager.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.azaldo.smartpantrymanager.database.DatabaseHelper;
import com.azaldo.smartpantrymanager.models.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all reads/writes for PantryItems. Activities and Adapters talk
 * to this class, never to DatabaseHelper or SQLiteDatabase directly - this
 * keeps the strict-matching logic (added later) and the UI code decoupled
 * from the raw database access.
 */
public class PantryRepository {

    private final DatabaseHelper databaseHelper;

    public PantryRepository(Context context) {
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Inserts a new pantry item. createdAt is stamped here so callers
     * never have to remember to set it.
     *
     * @return the new row's id, or -1 if the insert failed.
     */
    public long addItem(PantryItem item) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = buildContentValues(item);
        values.put(DatabaseHelper.COLUMN_PANTRY_CREATED_AT, System.currentTimeMillis());
        return db.insert(DatabaseHelper.TABLE_PANTRY_ITEMS, null, values);
    }

    /**
     * Updates an existing pantry item, matched by its id.
     *
     * @return the number of rows updated (0 or 1).
     */
    public int updateItem(PantryItem item) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = buildContentValues(item);
        return db.update(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                values,
                DatabaseHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    /**
     * Deletes a pantry item by id.
     *
     * @return the number of rows deleted (0 or 1).
     */
    public int deleteItem(long itemId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db.delete(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                DatabaseHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(itemId)}
        );
    }

    /** Returns a single pantry item by id, or null if it doesn't exist. */
    public PantryItem getItemById(long itemId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                null,
                DatabaseHelper.COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(itemId)},
                null, null, null
        );

        PantryItem item = null;
        if (cursor.moveToFirst()) {
            item = cursorToPantryItem(cursor);
        }
        cursor.close();
        return item;
    }

    /** Returns every pantry item, ordered by name, for the Pantry List screen. */
    public List<PantryItem> getAllItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                null, null, null, null, null,
                DatabaseHelper.COLUMN_PANTRY_NAME + " ASC"
        );

        while (cursor.moveToNext()) {
            items.add(cursorToPantryItem(cursor));
        }
        cursor.close();
        return items;
    }

    /**
     * Returns all pantry items whose normalizedName matches, used by the
     * RecipeMatcher to check whether an ingredient is available. Returns
     * an empty list (never null) if nothing matches.
     */
    public List<PantryItem> getItemsByNormalizedName(String normalizedName) {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PANTRY_ITEMS,
                null,
                DatabaseHelper.COLUMN_PANTRY_NORMALIZED_NAME + " = ?",
                new String[]{normalizedName},
                null, null, null
        );

        while (cursor.moveToNext()) {
            items.add(cursorToPantryItem(cursor));
        }
        cursor.close();
        return items;
    }

    /** Deletes every pantry item. Used by the "Clear pantry data" setting. */
    public void clearAllItems() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_PANTRY_ITEMS, null, null);
    }

    private ContentValues buildContentValues(PantryItem item) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PANTRY_NAME, item.getName());
        values.put(DatabaseHelper.COLUMN_PANTRY_NORMALIZED_NAME, item.getNormalizedName());
        values.put(DatabaseHelper.COLUMN_PANTRY_QUANTITY, item.getQuantity());
        values.put(DatabaseHelper.COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(DatabaseHelper.COLUMN_PANTRY_EXPIRY_DATE, item.getExpiryDate());
        return values;
    }

    private PantryItem cursorToPantryItem(Cursor cursor) {
        PantryItem item = new PantryItem();
        item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_ID)));
        item.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_NAME)));
        item.setNormalizedName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_NORMALIZED_NAME)));
        item.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_QUANTITY)));
        item.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_UNIT)));
        item.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_EXPIRY_DATE)));
        item.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PANTRY_CREATED_AT)));
        return item;
    }
}
