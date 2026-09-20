package com.azaldo.smartpantrymanager.database;

import com.azaldo.smartpantrymanager.models.Recipe;
import com.azaldo.smartpantrymanager.models.RecipeIngredient;
import com.azaldo.smartpantrymanager.repositories.RecipeRepository;
import com.azaldo.smartpantrymanager.utils.IngredientNormalizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Seeds the Recipes and RecipeIngredients tables with a starter collection
 * the first time the app runs. Guarded by RecipeRepository.hasAnyRecipes()
 * so it never inserts duplicates on later launches.
 *
 * Ingredient names here are written in a natural singular/plural form and
 * run through IngredientNormalizer before saving, exactly like a user
 * typing into the Add Ingredient screen would - this is what keeps the
 * seeded recipes comparable against pantry items later.
 */
public final class RecipeSeeder {

    private RecipeSeeder() {
    }

    public static void seedIfNeeded(RecipeRepository recipeRepository) {
        if (recipeRepository.hasAnyRecipes()) {
            return;
        }

        addRecipe(recipeRepository, "Scrambled Eggs",
                "A quick, simple breakfast staple.",
                "1. Crack the eggs into a bowl and whisk.\n2. Melt the butter in a pan over medium heat.\n3. Pour in the eggs, add salt, and stir gently until softly set.",
                ing("eggs", 3, "pieces"), ing("butter", 1, "tablespoons"), ing("salt", 1, "teaspoons"));

        addRecipe(recipeRepository, "Tomato Omelette",
                "A classic omelette with fresh tomato folded in.",
                "1. Whisk the eggs with salt.\n2. Dice the tomato.\n3. Cook the eggs in a pan, add tomato halfway through, fold and serve.",
                ing("eggs", 2, "pieces"), ing("tomatoes", 1, "pieces"), ing("salt", 1, "teaspoons"));

        addRecipe(recipeRepository, "Chicken and Rice",
                "A filling, one-pot dinner.",
                "1. Cook the rice according to package instructions.\n2. Season and pan-fry the chicken until cooked through.\n3. Saute the onion, combine everything, and serve.",
                ing("chicken", 500, "g"), ing("rice", 200, "g"), ing("onions", 1, "pieces"));

        addRecipe(recipeRepository, "Vegetable Fried Rice",
                "Leftover-friendly fried rice loaded with vegetables.",
                "1. Saute the onion and carrot.\n2. Push to the side, scramble the egg in the same pan.\n3. Add the rice, mix everything together, and season.",
                ing("rice", 300, "g"), ing("carrots", 1, "pieces"), ing("onions", 1, "pieces"), ing("eggs", 1, "pieces"));

        addRecipe(recipeRepository, "Tuna Sandwich",
                "A no-cook lunch option.",
                "1. Mix the tuna with mayonnaise.\n2. Spread onto one slice of bread.\n3. Top with the second slice and cut in half.",
                ing("bread", 2, "pieces"), ing("tuna", 150, "g"), ing("mayonnaise", 2, "tablespoons"));

        addRecipe(recipeRepository, "Pasta with Tomato Sauce",
                "A simple weeknight pasta.",
                "1. Boil the pasta until al dente.\n2. Saute garlic, add chopped tomatoes and simmer into a sauce.\n3. Toss the drained pasta through the sauce.",
                ing("pasta", 200, "g"), ing("tomatoes", 3, "pieces"), ing("garlic", 2, "pieces"));

        addRecipe(recipeRepository, "Potato Hash",
                "Crispy pan-fried potatoes and onion.",
                "1. Dice the potato and onion.\n2. Heat the oil in a pan.\n3. Fry, stirring occasionally, until golden and cooked through.",
                ing("potatoes", 3, "pieces"), ing("onions", 1, "pieces"), ing("cooking oil", 1, "tablespoons"));

        addRecipe(recipeRepository, "Chicken Pasta",
                "Pasta tossed with pan-seared chicken and garlic.",
                "1. Boil the pasta.\n2. Season and pan-fry the chicken, add garlic near the end.\n3. Combine chicken and pasta together.",
                ing("chicken", 300, "g"), ing("pasta", 200, "g"), ing("garlic", 2, "pieces"));

        addRecipe(recipeRepository, "Vegetable Soup",
                "A warming soup using pantry vegetables.",
                "1. Chop the carrot, potato and onion.\n2. Simmer all vegetables in water until tender.\n3. Season to taste and serve hot.",
                ing("carrots", 2, "pieces"), ing("potatoes", 2, "pieces"), ing("onions", 1, "pieces"));

        addRecipe(recipeRepository, "French Toast",
                "A sweet breakfast made from bread, egg and milk.",
                "1. Whisk the egg with the milk.\n2. Dip each slice of bread in the mixture.\n3. Pan-fry both sides until golden.",
                ing("bread", 4, "pieces"), ing("eggs", 2, "pieces"), ing("milk", 100, "ml"));

        addRecipe(recipeRepository, "Pancakes",
                "Simple stovetop pancakes.",
                "1. Whisk the flour, egg and milk into a batter.\n2. Pour small portions onto a hot pan.\n3. Flip once bubbles form on the surface.",
                ing("flour", 200, "g"), ing("eggs", 2, "pieces"), ing("milk", 250, "ml"));

        addRecipe(recipeRepository, "Egg Fried Rice",
                "A quick fried rice built around egg.",
                "1. Saute the onion.\n2. Push aside and scramble the egg.\n3. Add the rice and mix everything through.",
                ing("rice", 300, "g"), ing("eggs", 2, "pieces"), ing("onions", 1, "pieces"));

        addRecipe(recipeRepository, "Grilled Cheese Sandwich",
                "A pan-toasted cheese sandwich.",
                "1. Place cheese between the two slices of bread.\n2. Pan-fry on both sides until golden and the cheese melts.",
                ing("bread", 2, "pieces"), ing("cheese", 2, "pieces"));

        addRecipe(recipeRepository, "Garlic Pasta",
                "A simple garlic and oil pasta.",
                "1. Boil the pasta.\n2. Gently fry the garlic in the oil until fragrant.\n3. Toss the drained pasta through the garlic oil.",
                ing("pasta", 200, "g"), ing("garlic", 4, "pieces"), ing("cooking oil", 2, "tablespoons"));

        addRecipe(recipeRepository, "Chicken Stir-Fry",
                "A fast stir-fry with chicken and vegetables.",
                "1. Slice the chicken, onion and carrot.\n2. Stir-fry the chicken until cooked.\n3. Add the vegetables and stir-fry briefly, then serve.",
                ing("chicken", 300, "g"), ing("onions", 1, "pieces"), ing("carrots", 1, "pieces"));

        addRecipe(recipeRepository, "Tomato and Onion Salad",
                "A refreshing no-cook salad.",
                "1. Slice the tomato and onion.\n2. Combine in a bowl.\n3. Drizzle with oil and toss.",
                ing("tomatoes", 2, "pieces"), ing("onions", 1, "pieces"), ing("cooking oil", 1, "tablespoons"));

        addRecipe(recipeRepository, "Potato Omelette",
                "A hearty omelette with pan-fried potato.",
                "1. Dice and pan-fry the potato until tender.\n2. Whisk the eggs and pour over the potato.\n3. Cook until set, then fold and serve.",
                ing("potatoes", 2, "pieces"), ing("eggs", 3, "pieces"));

        addRecipe(recipeRepository, "Vegetable Pasta",
                "Pasta tossed with carrot and tomato.",
                "1. Boil the pasta.\n2. Saute the carrot and tomato until softened.\n3. Toss the drained pasta through the vegetables.",
                ing("pasta", 200, "g"), ing("carrots", 1, "pieces"), ing("tomatoes", 2, "pieces"));

        addRecipe(recipeRepository, "Rice and Beans",
                "A simple, filling combination.",
                "1. Cook the rice according to package instructions.\n2. Heat the beans through.\n3. Serve the beans over the rice.",
                ing("rice", 200, "g"), ing("beans", 200, "g"));

        addRecipe(recipeRepository, "Simple Chicken Curry",
                "An easy weeknight curry.",
                "1. Saute the onion until soft.\n2. Add the chicken and tomato, and simmer until the chicken is cooked through.\n3. Serve with rice.",
                ing("chicken", 400, "g"), ing("onions", 1, "pieces"), ing("tomatoes", 2, "pieces"));
    }

    /** Small helper to make each ingredient line in the calls above read as (name, quantity, unit). */
    private static Object[] ing(String name, double quantity, String unit) {
        return new Object[]{name, quantity, unit};
    }

    private static void addRecipe(RecipeRepository recipeRepository, String name, String description,
                                   String preparationSteps, Object[]... ingredientRows) {
        Recipe recipe = new Recipe(name, description, preparationSteps, null);

        List<RecipeIngredient> ingredients = new ArrayList<>();
        for (Object[] row : ingredientRows) {
            String ingredientName = (String) row[0];
            double quantity = (double) row[1];
            String unit = (String) row[2];
            ingredients.add(new RecipeIngredient(
                    0, // recipeId is set by the repository once the recipe row is inserted
                    ingredientName,
                    IngredientNormalizer.normalize(ingredientName),
                    quantity,
                    unit
            ));
        }

        recipeRepository.addRecipeWithIngredients(recipe, ingredients);
    }
}
