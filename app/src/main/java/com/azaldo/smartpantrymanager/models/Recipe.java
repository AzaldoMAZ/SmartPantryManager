package com.azaldo.smartpantrymanager.models;

/**
 * Represents a recipe. The ingredient requirements for a recipe live in
 * the separate RecipeIngredient table/model, linked by recipeId.
 */
public class Recipe {

    private long id;
    private String name;
    private String description;
    private String preparationSteps;
    private String imageResourceName; // optional local drawable name, may be null

    public Recipe() {
    }

    public Recipe(String name, String description, String preparationSteps, String imageResourceName) {
        this.name = name;
        this.description = description;
        this.preparationSteps = preparationSteps;
        this.imageResourceName = imageResourceName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreparationSteps() {
        return preparationSteps;
    }

    public void setPreparationSteps(String preparationSteps) {
        this.preparationSteps = preparationSteps;
    }

    public String getImageResourceName() {
        return imageResourceName;
    }

    public void setImageResourceName(String imageResourceName) {
        this.imageResourceName = imageResourceName;
    }
}
