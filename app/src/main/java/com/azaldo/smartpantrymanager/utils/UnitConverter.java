package com.azaldo.smartpantrymanager.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts a quantity between units, but only within the same physical
 * group. This is deliberately conservative per the assignment brief:
 *   - g and kg convert freely (weight)
 *   - ml and L convert freely (volume)
 *   - pieces, tablespoons, teaspoons and cups each form their own group
 *     and do NOT convert into anything else, because doing so (e.g.
 *     assuming 1 tablespoon = 15 g) would require an ingredient-specific
 *     density that this app does not model.
 *
 * convert() returns null when the two units are not compatible, and
 * RecipeMatcher treats null as "this pantry item cannot count toward this
 * requirement" rather than silently assuming a 1:1 conversion.
 */
public final class UnitConverter {

    private static final Map<String, String> UNIT_GROUP = new HashMap<>();
    private static final Map<String, Double> UNIT_TO_BASE = new HashMap<>();

    static {
        // Weight group - base unit is grams.
        UNIT_GROUP.put("g", "weight");
        UNIT_GROUP.put("kg", "weight");
        UNIT_TO_BASE.put("g", 1.0);
        UNIT_TO_BASE.put("kg", 1000.0);

        // Volume group - base unit is millilitres.
        UNIT_GROUP.put("ml", "volume");
        UNIT_GROUP.put("l", "volume");
        UNIT_TO_BASE.put("ml", 1.0);
        UNIT_TO_BASE.put("l", 1000.0);

        // Everything else is its own standalone group - no cross-conversion.
        UNIT_GROUP.put("pieces", "pieces");
        UNIT_TO_BASE.put("pieces", 1.0);

        UNIT_GROUP.put("tablespoons", "tablespoons");
        UNIT_TO_BASE.put("tablespoons", 1.0);

        UNIT_GROUP.put("teaspoons", "teaspoons");
        UNIT_TO_BASE.put("teaspoons", 1.0);

        UNIT_GROUP.put("cups", "cups");
        UNIT_TO_BASE.put("cups", 1.0);
    }

    private UnitConverter() {
    }

    /**
     * Converts quantity from fromUnit into toUnit.
     *
     * @return the converted quantity, or null if fromUnit and toUnit are
     * not in the same compatible group (e.g. g to pieces).
     */
    public static Double convert(double quantity, String fromUnit, String toUnit) {
        if (fromUnit == null || toUnit == null) {
            return null;
        }

        String from = fromUnit.toLowerCase().trim();
        String to = toUnit.toLowerCase().trim();

        if (from.equals(to)) {
            return quantity;
        }

        String fromGroup = UNIT_GROUP.get(from);
        String toGroup = UNIT_GROUP.get(to);

        if (fromGroup == null || toGroup == null || !fromGroup.equals(toGroup)) {
            return null;
        }

        double quantityInBaseUnits = quantity * UNIT_TO_BASE.get(from);
        return quantityInBaseUnits / UNIT_TO_BASE.get(to);
    }
}
