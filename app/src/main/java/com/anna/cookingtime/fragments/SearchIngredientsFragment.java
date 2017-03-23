package com.anna.cookingtime.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anna.cookingtime.CookingTimeApp;
import com.anna.cookingtime.R;
import com.anna.cookingtime.activities.MainActivity;
import com.anna.cookingtime.adapters.IngredientsRecyclerViewAdapter;
import com.anna.cookingtime.interfaces.RecyclerViewTouchListener;
import com.anna.cookingtime.models.BaseArrayModel;
import com.anna.cookingtime.models.CategoriesModel;
import com.anna.cookingtime.models.Category;
import com.anna.cookingtime.models.Ingredients;
import com.anna.cookingtime.utils.Constants;
import com.anna.cookingtime.views.RecycleViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iva on 18.02.17.
 */

public class SearchIngredientsFragment extends BaseFragment {
    public static final String TAG = "SearchIngredients";
    private static final byte INGREDIENTS_LIMIT = 25;

    @BindView(R.id.dishesRecycler)
    RecyclerView categoryRecycler;
    @BindView(R.id.swipeToRefreshLayout)
    PtrFrameLayout swipeToRefreshLayout;
    @BindView(R.id.findButton)
    Button findButton;

    private List<Ingredients> ingredientsList;
    private byte page = 1;
    private int nextPage = 1;
    private IngredientsRecyclerViewAdapter ingredientsAdapter;
    private String searchQuery = "";
    private boolean isSearch = false;
    private ArrayList<Integer> selectedIngredients;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_name, container, false);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // empty click caused adding fragments not replacing!!!
            }
        });

        ButterKnife.bind(this, root);

        findButton.setVisibility(View.VISIBLE);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.ingredients_toolbar));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Ingredients> ingredientsList = (List<Ingredients>) cacheManager.getObjectFromCache(TAG);
        if (ingredientsList != null) {
            initRecyclerView(ingredientsList);
        }

        initSwipeToRefresh();


        if (!cacheManager.getRefreshFragmentMap().containsKey(TAG)) {
            swipeToRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeToRefreshLayout.autoRefresh();
                }
            }, 100);
            cacheManager.getRefreshFragmentMap().put(TAG, true);
        }
    }

    private void initSwipeToRefresh() {
        final MaterialHeader header = new MaterialHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.swipe_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(
                0,
                getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin),
                0,
                getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        );
        header.setPtrFrameLayout(swipeToRefreshLayout);

        swipeToRefreshLayout.setHeaderView(header);
        swipeToRefreshLayout.addPtrUIHandler(header);
        swipeToRefreshLayout.setPtrHandler(new PtrHandler() {


            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                page = 1;
                if (isSearch) {
                    searchIngredients();
                } else {
                    getIngredients();
                }
            }
        });
    }

    private void initRecyclerView(List<Ingredients> ingredientsList) {
        if (ingredientsList != null) {
            this.ingredientsList = ingredientsList;
            if (ingredientsAdapter != null && categoryRecycler.getAdapter() != null) {
                if (SearchIngredientsFragment.this.page != 1) {
                    ingredientsAdapter.addData(ingredientsList);
                } else {
                    ingredientsAdapter.setNewData(ingredientsList, selectedIngredients);
                }
            } else {
                ingredientsAdapter = new IngredientsRecyclerViewAdapter(
                        ingredientsList,
                        new ArrayList<Integer>()
                );
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                        CookingTimeApp.getAppContext()
                );

                categoryRecycler.setLayoutManager(mLayoutManager);
                final LinearLayoutManager layoutManager = (LinearLayoutManager) categoryRecycler.getLayoutManager();

                categoryRecycler.addItemDecoration(
                        new RecycleViewItemDecoration(
                                CookingTimeApp.getAppContext(),
                                LinearLayoutManager.VERTICAL
                        )
                );
                categoryRecycler.setAdapter(ingredientsAdapter);

                categoryRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int lastShowPost = layoutManager.findLastVisibleItemPosition();

                        if ((lastShowPost + 1) == (INGREDIENTS_LIMIT * SearchIngredientsFragment.this.page) && SearchIngredientsFragment.this.nextPage != SearchIngredientsFragment.this.page) {
                            page++;
                            getIngredients();
                        }

                    }
                });

                categoryRecycler.addOnItemTouchListener(
                        new RecyclerViewTouchListener(
                                CookingTimeApp.getAppContext(),
                                categoryRecycler,
                                new RecyclerViewTouchListener.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }

                                    @Override
                                    public void onItemDoubleClick(View view, int position) {

                                    }
                                }
                        )
                );
            }
        }
    }

    private void getIngredients() {
        getCalls().getAllIngredients(page).enqueue(new Callback<BaseArrayModel<Ingredients>>() {
            @Override
            public void onResponse(Call<BaseArrayModel<Ingredients>> call, Response<BaseArrayModel<Ingredients>> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToRefreshLayout.refreshComplete();
                    }
                });
                BaseArrayModel<Ingredients> ingredients = response.body();
                if (ingredients != null) {
                    SearchIngredientsFragment.this.nextPage = ingredients.getNextPage();
                    if (!ingredients.getData().isEmpty()) {
                        if (page == 1) {
                            cacheManager.putOrUpdateCache(TAG, ingredients.getData());
                        } else {
                            List<Ingredients> federal = (List<Ingredients>) cacheManager.getObjectFromCache(TAG);
                            federal.addAll(ingredients.getData());
                            cacheManager.putOrUpdateCache(TAG, federal);
                        }
                        initRecyclerView(ingredients.getData());
                    } else {
                        cacheManager.putOrUpdateCache(TAG, ingredients.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseArrayModel<Ingredients>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToRefreshLayout.refreshComplete();
                    }
                });
            }
        });
    }

    private void searchIngredients() {
        selectedIngredients = ingredientsAdapter.getSelectedList();
        getCalls().searchIngredient(page, searchQuery).enqueue(new Callback<BaseArrayModel<Ingredients>>() {
            @Override
            public void onResponse(Call<BaseArrayModel<Ingredients>> call, Response<BaseArrayModel<Ingredients>> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToRefreshLayout.refreshComplete();
                    }
                });
                BaseArrayModel<Ingredients> ingredients = response.body();
                if (ingredients != null) {
                    SearchIngredientsFragment.this.nextPage = ingredients.getNextPage();
                    if (!ingredients.getData().isEmpty()) {
                        initRecyclerView(ingredients.getData());

                    }
                }
            }

            @Override
            public void onFailure(Call<BaseArrayModel<Ingredients>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToRefreshLayout.refreshComplete();
                    }
                });
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.input_ingredient_name));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                page = 1;
                searchQuery = query;
                swipeToRefreshLayout.autoRefresh();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                isSearch = true;
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                isSearch = false;
                swipeToRefreshLayout.autoRefresh();
                return true;
            }
        });
    }

    @OnClick(R.id.findButton)
    public void onClick() {
        requestListener.startSearchDishFragment(
                Constants.INGREDIENTS_TYPE,
                ingredientsAdapter.getSelectedList()
        );
    }
}
