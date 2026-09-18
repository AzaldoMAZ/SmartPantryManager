package com.azaldo.smartpantrymanager.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Normalizes ingredient names so that "Tomato", "tomatoes" and "TOMATO "
 * are all recognised as the same ingredient when the RecipeMatcher
 * (added later) compares pantry items against recipe requirements.
 *
 * This is deliberately simple - not a full NLP solution - per the
 * assignment brief. It handles:
 *   1. Lowercasing
 *   2. Trimming leading/trailing whitespace
 *   3. Collapsing internal whitespace and removing punctuation
 *   4. A small, explicit alias map for common singular/plural pairs
 *      that don't follow the simple "just drop the trailing s" rule
 *   5. A basic trailing "s" removal for anything not already in the map
 */
public final class IngredientNormalizer {

    // Explicit aliases for words where naive plural-stripping would be
    // wrong or where a common alternate spelling should map to one form.
    private static final Map<String, String> ALIASES = new HashMap<>();

    static {
        ALIASES.put("tomatoes", "tomato");
        ALIASES.put("potatoes", "potato");
        ALIASES.put("onions", "onion");
        ALIASES.put("tomatos", "tomato"); // common misspelling
        ALIASES.put("potatos", "potato"); // common misspelling
        ALIASES.put("loaves", "loaf");
        ALIASES.put("leaves", "leaf");
    }

    private IngredientNormalizer() {
        // Utility class - no instances.
    }

    /**
     * Returns the normalized form of an ingredient name, used both when
     * saving a pantry item and when saving a recipe's required ingredient,
     * so the two can be compared reliably later.
     */
    public static String normalize(String rawName) {
        if (rawName == null) {
            return "";
        }

        String name = rawName.toLowerCase().trim();

        // Remove punctuation (keep letters, digits and spaces only), then
        // collapse any resulting double spaces from removed punctuation.
        name = name.replaceAll("[^a-z0-9\\s]", "");
        name = name.replaceAll("\\s+", " ").trim();

        if (ALIASES.containsKey(name)) {
            return ALIASES.get(name);
        }

        // Basic singular/plural handling: if the word ends in "s" but not
        // "ss" (e.g. avoid turning "swiss" into "swis"), strip the "s".
        // This is a simple heuristic, not a full pluralization ruleset,
        // and is acceptable per the assignment's "no full NLP" guidance.
        if (name.endsWith("s") && !name.endsWith("ss") && name.length() > 3) {
            return name.substring(0, name.length() - 1);
        }

        return name;
    }
}
