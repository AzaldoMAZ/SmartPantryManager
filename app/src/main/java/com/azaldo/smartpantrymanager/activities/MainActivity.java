package com.azaldo.smartpantrymanager.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azaldo.smartpantrymanager.R;

/**
 * MainActivity displays the Pantry List screen - the app's entry point.
 *
 * At this stage the layout and RecyclerView shell are wired up, but there
 * is no data source connected yet. The real PantryAdapter and
 * PantryRepository are added once the database layer exists (next commit),
 * at which point this Activity will load real pantry items instead of
 * always showing the empty state.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private TextView textEmptyPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        textEmptyPantry = findViewById(R.id.textEmptyPantry);

        recyclerViewPantry.setLayoutManager(new LinearLayoutManager(this));

        // Placeholder until PantryRepository is implemented.
        showEmptyState(true);
    }

    private void showEmptyState(boolean isEmpty) {
        textEmptyPantry.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerViewPantry.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}
