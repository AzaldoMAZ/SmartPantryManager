package com.azaldo.smartpantrymanager;

import android.app.Application;

import com.azaldo.smartpantrymanager.database.RecipeSeeder;
import com.azaldo.smartpantrymanager.repositories.RecipeRepository;

/**
 * Custom Application class so recipe seeding runs exactly once, before any
 * Activity needs the recipe data, rather than being triggered from
 * MainActivity (which would run it every time the Pantry List screen opens).
 */
public class SmartPantryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RecipeRepository recipeRepository = new RecipeRepository(this);
        RecipeSeeder.seedIfNeeded(recipeRepository);
    }
}
