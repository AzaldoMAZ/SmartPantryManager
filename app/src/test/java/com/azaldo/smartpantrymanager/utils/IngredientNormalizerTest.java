package com.azaldo.smartpantrymanager.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Covers test cases 4 and 5 from the assignment's required test list:
 * different capitalization, and singular/plural ingredient names.
 */
public class IngredientNormalizerTest {

    @Test
    public void lowercasesAndTrims() {
        assertEquals("tomato", IngredientNormalizer.normalize("  Tomato  "));
        assertEquals("tomato", IngredientNormalizer.normalize("TOMATO"));
    }

    @Test
    public void handlesSimplePlurals() {
        assertEquals("carrot", IngredientNormalizer.normalize("carrots"));
        assertEquals("egg", IngredientNormalizer.normalize("eggs"));
    }

    @Test
    public void handlesExplicitAliases() {
        assertEquals("tomato", IngredientNormalizer.normalize("tomatoes"));
        assertEquals("potato", IngredientNormalizer.normalize("potatoes"));
        assertEquals("onion", IngredientNormalizer.normalize("onions"));
    }

    @Test
    public void doesNotBreakWordsEndingInDoubleS() {
        // Guards against the naive "strip trailing s" rule mangling words
        // like "swiss" into "swis".
        assertEquals("swiss", IngredientNormalizer.normalize("Swiss"));
    }

    @Test
    public void removesPunctuationAndCollapsesSpaces() {
        assertEquals("cooking oil", IngredientNormalizer.normalize("Cooking   Oil!"));
    }
}
