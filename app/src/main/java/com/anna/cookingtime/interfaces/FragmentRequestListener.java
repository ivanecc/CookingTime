package com.anna.cookingtime.interfaces;

/**
 * Created by iva on 19.02.17.
 */

public interface FragmentRequestListener {
    void startSearchDishFragment(int type, long idCategory);
    void startSearchNameFragment();
    void startSearchCategoriesFragment();
    void startSearchIngredientsFragment();
    void startDish(long id);
}
