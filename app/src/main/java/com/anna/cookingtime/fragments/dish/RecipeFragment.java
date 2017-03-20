package com.anna.cookingtime.fragments.dish;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anna.cookingtime.R;
import com.anna.cookingtime.fragments.BaseFragment;
import com.anna.cookingtime.models.Dish;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iva on 18.03.17.
 */

public class RecipeFragment extends BaseFragment implements DishFragment.RefreshData {
    private static final String TAG = "RecipeFragment";

    @BindView(R.id.recipe)
    TextView recipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dish_detail, container, false);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // empty click caused adding fragments not replacing!!!
            }
        });

        ButterKnife.bind(this, root);

        ((DishFragment) getParentFragment()).setDetailsListener(RecipeFragment.this);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (((DishFragment)getParentFragment()).dish != null) {
            recipe.setText(((DishFragment) getParentFragment()).dish.getInstructions());
        }
    }

    @Override
    public void onRefresh(Dish dish) {
        recipe.setText(dish.getInstructions());
    }
}
