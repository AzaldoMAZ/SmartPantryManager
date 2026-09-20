package com.azaldo.smartpantrymanager.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Covers test cases 8 and 9 from the assignment's required test list:
 * compatible unit conversion, and incompatible units.
 */
public class UnitConverterTest {

    private static final double DELTA = 0.0001;

    @Test
    public void sameUnitReturnsSameQuantity() {
        assertEquals(500.0, UnitConverter.convert(500, "g", "g"), DELTA);
    }

    @Test
    public void convertsKgToG() {
        // 1 kg pantry item should satisfy a 500 g requirement.
        assertEquals(1000.0, UnitConverter.convert(1, "kg", "g"), DELTA);
    }

    @Test
    public void convertsLToMl() {
        assertEquals(1000.0, UnitConverter.convert(1, "L", "ml"), DELTA);
    }

    @Test
    public void refusesIncompatibleWeightAndPieces() {
        // 500 g must never be silently treated as 500 pieces.
        assertNull(UnitConverter.convert(500, "g", "pieces"));
    }

    @Test
    public void refusesIncompatibleVolumeAndWeight() {
        // 1 L must never be silently treated as 1 kg.
        assertNull(UnitConverter.convert(1, "L", "kg"));
    }

    @Test
    public void refusesTablespoonToGram() {
        // No documented density conversion exists, so this must return null.
        assertNull(UnitConverter.convert(1, "tablespoons", "g"));
    }
}
