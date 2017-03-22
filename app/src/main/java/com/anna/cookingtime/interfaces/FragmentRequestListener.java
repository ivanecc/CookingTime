package com.anna.cookingtime.interfaces;

import java.util.ArrayList;

/**
 * Created by iva on 19.02.17.
 */

public interface FragmentRequestListener {
    void startSearchDishFragment(int type, long idCategory, String name);
    void startSearchDishFragment(int type, ArrayList<Integer> idList);
    void startSearchNameFragment();
    void startSearchCategoriesFragment();
    void startSearchIngredientsFragment();
    void startDish(long id);
}
