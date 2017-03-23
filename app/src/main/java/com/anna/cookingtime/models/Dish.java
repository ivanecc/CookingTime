package com.anna.cookingtime.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iva on 21.02.17.
 */

public class Dish implements Serializable {
    private long id;
    private String name;
    private String instructions;
    private String imageUrl;
    private byte difficultyLevel;
    private int cookingTime;
    private int calories;
    private List<Ingredients> ingredients;
    private transient boolean isFavorite;

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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(byte difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
