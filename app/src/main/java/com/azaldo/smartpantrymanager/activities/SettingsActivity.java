package com.azaldo.smartpantrymanager.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.azaldo.smartpantrymanager.R;
import com.azaldo.smartpantrymanager.repositories.PantryRepository;
import com.azaldo.smartpantrymanager.repositories.SettingsRepository;

/**
 * Settings screen. Two settings are genuinely functional rather than
 * decorative:
 *   - Expiry alerts toggle: persisted immediately on change.
 *   - Preferred unit: persisted immediately on selection.
 * "Clear pantry data" is a real destructive action, guarded by a
 * confirmation dialog per the assignment's requirement.
 */
public class SettingsActivity extends AppCompatActivity {

    private SettingsRepository settingsRepository;
    private PantryRepository pantryRepository;

    // Guards against the Spinner's onItemSelected firing once automatically
    // when setSelection() is called during setup, which would otherwise
    // immediately re-save the value we just loaded.
    private boolean isSpinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        settingsRepository = new SettingsRepository(this);
        pantryRepository = new PantryRepository(this);

        setUpExpiryAlertsSwitch();
        setUpPreferredUnitSpinner();
        setUpClearPantryButton();
    }

    private void setUpExpiryAlertsSwitch() {
        Switch switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        switchExpiryAlerts.setChecked(settingsRepository.isExpiryAlertsEnabled());
        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) ->
                settingsRepository.setExpiryAlertsEnabled(isChecked));
    }

    private void setUpPreferredUnitSpinner() {
        Spinner spinnerPreferredUnit = findViewById(R.id.spinnerPreferredUnit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPreferredUnit.setAdapter(adapter);

        int savedPosition = adapter.getPosition(settingsRepository.getPreferredUnit());
        if (savedPosition >= 0) {
            spinnerPreferredUnit.setSelection(savedPosition);
        }

        spinnerPreferredUnit.post(() -> isSpinnerInitialized = true);

        spinnerPreferredUnit.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (isSpinnerInitialized) {
                    settingsRepository.setPreferredUnit(parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // No action needed.
            }
        });
    }

    private void setUpClearPantryButton() {
        findViewById(R.id.buttonClearPantry).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Clear pantry data")
                        .setMessage("This will permanently delete every ingredient in your pantry. This cannot be undone.")
                        .setPositiveButton("Clear", (dialog, which) -> {
                            pantryRepository.clearAllItems();
                            Toast.makeText(this, "Pantry data cleared", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show());
    }
}
