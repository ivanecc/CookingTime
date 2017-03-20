package com.anna.cookingtime.fragments.dish;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anna.cookingtime.CookingTimeApp;
import com.anna.cookingtime.R;
import com.anna.cookingtime.adapters.IngredientsRecyclerViewAdapter;
import com.anna.cookingtime.fragments.BaseFragment;
import com.anna.cookingtime.fragments.SearchNameFragment;
import com.anna.cookingtime.interfaces.RecyclerViewTouchListener;
import com.anna.cookingtime.models.Dish;
import com.anna.cookingtime.models.Ingredients;
import com.anna.cookingtime.views.RecycleViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iva on 18.03.17.
 */

public class IngredientsFragment extends BaseFragment implements DishFragment.RefreshData {
    public static final String TAG = "IngredientsFragment";

    @BindView(R.id.dishIngredients)
    RecyclerView recyclerIngredients;

    IngredientsRecyclerViewAdapter ingredientsAdapter;

    private Dish dish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dish_ingredients, container, false);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // empty click caused adding fragments not replacing!!!
            }
        });

        ButterKnife.bind(this, root);

        ((DishFragment) getParentFragment()).setIngredientsListener(IngredientsFragment.this);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dish = ((DishFragment) getParentFragment()).dish;
        if (dish != null) {
            initRecyclerView(dish.getIngredients());
        }
    }

    private void initRecyclerView(List<Ingredients> ingredientsList) {
        if (ingredientsList != null) {
            if (ingredientsAdapter != null && recyclerIngredients.getAdapter() != null) {
                ingredientsAdapter.setNewData(ingredientsList);
            } else {
                ingredientsAdapter = new IngredientsRecyclerViewAdapter(ingredientsList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                        CookingTimeApp.getAppContext()
                );

                recyclerIngredients.setLayoutManager(mLayoutManager);

                recyclerIngredients.addItemDecoration(
                        new RecycleViewItemDecoration(
                                CookingTimeApp.getAppContext(),
                                LinearLayoutManager.VERTICAL
                        )
                );

                recyclerIngredients.setAdapter(ingredientsAdapter);
            }
        }
    }

    @Override
    public void onRefresh(Dish dish) {
        initRecyclerView(dish.getIngredients());
    }
}
