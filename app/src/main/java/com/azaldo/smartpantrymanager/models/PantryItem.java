package com.azaldo.smartpantrymanager.models;

/**
 * Represents a single ingredient the user has at home.
 * Maps directly to a row in the PantryItems table.
 */
public class PantryItem {

    private long id;
    private String name;
    private String normalizedName;
    private double quantity;
    private String unit;
    private String expiryDate; // stored as ISO date string, e.g. "2026-09-30"; null/empty if not set
    private long createdAt;    // stored as epoch millis

    public PantryItem() {
    }

    public PantryItem(String name, String normalizedName, double quantity, String unit, String expiryDate) {
        this.name = name;
        this.normalizedName = normalizedName;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
