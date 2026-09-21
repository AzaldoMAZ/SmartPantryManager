package com.azaldo.smartpantrymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        fabAddIngredient.setOnClickListener(v ->
                startActivity(AddEditIngredientActivity.newAddIntent(this)));

        findViewById(R.id.buttonEmptyAddIngredient).setOnClickListener(v ->
                startActivity(AddEditIngredientActivity.newAddIntent(this)));
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
        findViewById(R.id.buttonEmptyAddIngredient).setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerViewPantry.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onEditClicked(PantryItem item) {
        startActivity(AddEditIngredientActivity.newEditIntent(this, item.getId()));
    }

    @Override
    public void onDeleteClicked(PantryItem item) {
        pantryRepository.deleteItem(item.getId());
        Toast.makeText(this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
        refreshPantryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_suggested_recipes) {
            startActivity(new Intent(this, SuggestedRecipesActivity.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
