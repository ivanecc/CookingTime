package com.anna.cookingtime.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.anna.cookingtime.fragments.dish.IngredientsFragment;
import com.anna.cookingtime.fragments.dish.RecipeFragment;
import com.anna.cookingtime.utils.Constants;
import com.anna.cookingtime.utils.Utils;

/**
 * Created by iva on 17.03.17.
 */

public class DishViewPagerAdapter extends SmartFragmentStatePagerAdapter {
    private int NUM_ITEMS = 2;

    public DishViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.RECIPE:
                return new RecipeFragment();
            case Constants.INGREDIENTS:
                return new IngredientsFragment();
            default:
//                return new FeedFragment();
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
