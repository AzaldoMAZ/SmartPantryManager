package com.azaldo.smartpantrymanager.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.azaldo.smartpantrymanager.database.DatabaseHelper;

/**
 * Reads and writes the single Settings row (id is always 1, seeded by
 * DatabaseHelper.onCreate). Kept as its own repository, separate from
 * Pantry/Recipe, since it has a completely different shape - one row,
 * updated in place, never inserted or deleted by the app.
 */
public class SettingsRepository {

    private final DatabaseHelper databaseHelper;

    public SettingsRepository(Context context) {
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    public boolean isExpiryAlertsEnabled() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_SETTINGS,
                new String[]{DatabaseHelper.COLUMN_SETTINGS_EXPIRY_ALERTS_ENABLED},
                null, null, null, null, null
        );

        boolean enabled = true; // default, matches the seeded row
        if (cursor.moveToFirst()) {
            enabled = cursor.getInt(0) == 1;
        }
        cursor.close();
        return enabled;
    }

    public String getPreferredUnit() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_SETTINGS,
                new String[]{DatabaseHelper.COLUMN_SETTINGS_PREFERRED_UNIT},
                null, null, null, null, null
        );

        String unit = "g"; // default, matches the seeded row
        if (cursor.moveToFirst()) {
            unit = cursor.getString(0);
        }
        cursor.close();
        return unit;
    }

    public void setExpiryAlertsEnabled(boolean enabled) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SETTINGS_EXPIRY_ALERTS_ENABLED, enabled ? 1 : 0);
        db.update(DatabaseHelper.TABLE_SETTINGS, values,
                DatabaseHelper.COLUMN_SETTINGS_ID + " = 1", null);
    }

    public void setPreferredUnit(String unit) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SETTINGS_PREFERRED_UNIT, unit);
        db.update(DatabaseHelper.TABLE_SETTINGS, values,
                DatabaseHelper.COLUMN_SETTINGS_ID + " = 1", null);
    }
}
