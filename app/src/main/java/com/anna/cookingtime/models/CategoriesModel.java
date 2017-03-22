package com.anna.cookingtime.models;

import java.util.List;

/**
 * Created by iva on 21.03.17.
 */

public class CategoriesModel {
    private List<Category> dishes;

    public void setCategories(List<Category> categoryList) {
        this.dishes = categoryList;
    }

    public List<Category> getCategories() {
        return this.dishes;
    }
}
