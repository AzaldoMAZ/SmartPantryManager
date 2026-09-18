package com.azaldo.smartpantrymanager.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azaldo.smartpantrymanager.R;
import com.azaldo.smartpantrymanager.adapters.PantryAdapter;
import com.azaldo.smartpantrymanager.models.PantryItem;
import com.azaldo.smartpantrymanager.repositories.PantryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity displays the Pantry List screen - the app's entry point.
 *
 * Data is reloaded in onResume (not just onCreate) so that returning from
 * the Add/Edit Ingredient screen always shows up-to-date data, without
 * needing to pass results back through startActivityForResult.
 */
public class MainActivity extends AppCompatActivity implements PantryAdapter.OnPantryItemClickListener {

    private RecyclerView recyclerViewPantry;
    private TextView textEmptyPantry;
    private PantryRepository pantryRepository;
    private PantryAdapter pantryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantryRepository = new PantryRepository(this);

        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        textEmptyPantry = findViewById(R.id.textEmptyPantry);
        View fabAddIngredient = findViewById(R.id.fabAddIngredient);

        recyclerViewPantry.setLayoutManager(new LinearLayoutManager(this));
        pantryAdapter = new PantryAdapter(new ArrayList<>(), this);
        recyclerViewPantry.setAdapter(pantryAdapter);

        fabAddIngredient.setOnClickListener(v -> {
            // AddEditIngredientActivity is added in the next commit.
            // Wired here so the click target already exists once it lands.
            Toast.makeText(this, "Add ingredient screen coming next", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPantryList();
    }

    private void refreshPantryList() {
        List<PantryItem> items = pantryRepository.getAllItems();
        pantryAdapter.updateData(items);
        showEmptyState(items.isEmpty());
    }

    private void showEmptyState(boolean isEmpty) {
        textEmptyPantry.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerViewPantry.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onEditClicked(PantryItem item) {
        // Wired up to AddEditIngredientActivity in the next commit.
        Toast.makeText(this, "Edit: " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClicked(PantryItem item) {
        pantryRepository.deleteItem(item.getId());
        Toast.makeText(this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
        refreshPantryList();
    }
}
