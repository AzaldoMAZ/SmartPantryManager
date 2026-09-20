package com.azaldo.smartpantrymanager.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.azaldo.smartpantrymanager.R;
import com.azaldo.smartpantrymanager.models.PantryItem;
import com.azaldo.smartpantrymanager.repositories.PantryRepository;
import com.azaldo.smartpantrymanager.utils.IngredientNormalizer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

/**
 * Handles both adding a new pantry item and editing an existing one.
 *
 * If the Intent that started this Activity carries EXTRA_ITEM_ID, the
 * screen loads that item and switches into "edit" mode; otherwise it
 * behaves as "add new". This avoids having two near-identical Activities
 * for what is functionally the same form.
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";
    private static final long NO_ITEM_ID = -1L;

    private TextInputLayout layoutIngredientName;
    private TextInputLayout layoutQuantity;
    private TextInputLayout layoutExpiryDate;
    private TextInputEditText editIngredientName;
    private TextInputEditText editQuantity;
    private TextInputEditText editExpiryDate;
    private Spinner spinnerUnit;

    private PantryRepository pantryRepository;
    private long editingItemId = NO_ITEM_ID;
    private String selectedExpiryDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        pantryRepository = new PantryRepository(this);
        bindViews();
        setUpUnitSpinner();
        setUpExpiryDatePicker();

        editingItemId = getIntent().getLongExtra(EXTRA_ITEM_ID, NO_ITEM_ID);
        if (editingItemId != NO_ITEM_ID) {
            setTitle("Edit Ingredient");
            loadExistingItem(editingItemId);
        } else {
            setTitle("Add Ingredient");
        }

        findViewById(R.id.buttonSaveIngredient).setOnClickListener(v -> attemptSave());
    }

    private void bindViews() {
        layoutIngredientName = findViewById(R.id.layoutIngredientName);
        layoutQuantity = findViewById(R.id.layoutQuantity);
        layoutExpiryDate = findViewById(R.id.layoutExpiryDate);
        editIngredientName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiryDate = findViewById(R.id.editExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);
    }

    private void setUpUnitSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);
    }

    private void setUpExpiryDatePicker() {
        editExpiryDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        selectedExpiryDate = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        editExpiryDate.setText(selectedExpiryDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            // Expiry dates in the past aren't useful for this app.
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000L);
            dialog.show();
        });
    }

    private void loadExistingItem(long itemId) {
        PantryItem item = pantryRepository.getItemById(itemId);
        if (item == null) {
            Toast.makeText(this, "Ingredient not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editIngredientName.setText(item.getName());
        editQuantity.setText(formatQuantityForEditing(item.getQuantity()));

        ArrayAdapter adapter = (ArrayAdapter) spinnerUnit.getAdapter();
        int unitPosition = adapter.getPosition(item.getUnit());
        if (unitPosition >= 0) {
            spinnerUnit.setSelection(unitPosition);
        }

        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            selectedExpiryDate = item.getExpiryDate();
            editExpiryDate.setText(selectedExpiryDate);
        }
    }

    private String formatQuantityForEditing(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }

    /**
     * Validates every field per the assignment's requirements, showing
     * inline errors via TextInputLayout rather than a generic Toast, then
     * saves through PantryRepository if everything passes.
     */
    private void attemptSave() {
        layoutIngredientName.setError(null);
        layoutQuantity.setError(null);

        String name = safeText(editIngredientName);
        String quantityRaw = safeText(editQuantity);

        boolean isValid = true;

        if (name.isEmpty()) {
            layoutIngredientName.setError(getString(R.string.error_name_required));
            isValid = false;
        }

        double quantity = 0;
        if (quantityRaw.isEmpty()) {
            layoutQuantity.setError(getString(R.string.error_quantity_invalid));
            isValid = false;
        } else {
            try {
                quantity = Double.parseDouble(quantityRaw);
                if (quantity <= 0) {
                    layoutQuantity.setError(getString(R.string.error_quantity_invalid));
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                layoutQuantity.setError(getString(R.string.error_quantity_invalid));
                isValid = false;
            }
        }

        if (spinnerUnit.getSelectedItem() == null) {
            Toast.makeText(this, R.string.error_unit_required, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        String unit = spinnerUnit.getSelectedItem().toString();

        PantryItem item = new PantryItem(
                name,
                IngredientNormalizer.normalize(name),
                quantity,
                unit,
                selectedExpiryDate.isEmpty() ? null : selectedExpiryDate
        );

        if (editingItemId != NO_ITEM_ID) {
            item.setId(editingItemId);
            pantryRepository.updateItem(item);
            Toast.makeText(this, name + " updated", Toast.LENGTH_SHORT).show();
        } else {
            pantryRepository.addItem(item);
            Toast.makeText(this, name + " added", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    private String safeText(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }

    /** Convenience for MainActivity to launch this screen in "add" mode. */
    public static Intent newAddIntent(android.content.Context context) {
        return new Intent(context, AddEditIngredientActivity.class);
    }

    /** Convenience for MainActivity to launch this screen in "edit" mode. */
    public static Intent newEditIntent(android.content.Context context, long itemId) {
        Intent intent = new Intent(context, AddEditIngredientActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        return intent;
    }
}
