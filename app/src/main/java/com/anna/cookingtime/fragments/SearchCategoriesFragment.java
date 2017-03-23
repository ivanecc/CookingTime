package com.anna.cookingtime.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.anna.cookingtime.CookingTimeApp;
import com.anna.cookingtime.R;
import com.anna.cookingtime.activities.MainActivity;
import com.anna.cookingtime.adapters.CategoriesRecyclerViewAdapter;
import com.anna.cookingtime.interfaces.RecyclerViewTouchListener;
import com.anna.cookingtime.models.BaseArrayModel;
import com.anna.cookingtime.models.CategoriesModel;
import com.anna.cookingtime.models.Category;
import com.anna.cookingtime.models.Dish;
import com.anna.cookingtime.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class SearchCategoriesFragment extends BaseFragment {
    public static final String TAG = "SearchCategories";

    @BindView(R.id.dishesRecycler)
    RecyclerView categoryRecycler;
    @BindView(R.id.swipeToRefreshLayout)
    PtrFrameLayout swipeToRefreshLayout;

    private List<Category> categoryList;
    private CategoriesRecyclerViewAdapter categoryAdapter;

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

        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.categories_toolbar));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Category> categoryList = (List<Category>) cacheManager.getObjectFromCache(TAG);
        if (categoryList != null) {
            initRecyclerView(categoryList);
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
                getCategories();
            }
        });
    }

    private void initRecyclerView(List<Category> categoryList) {
        if (categoryList != null) {
            this.categoryList = categoryList;
            if (categoryAdapter != null && categoryRecycler.getAdapter() != null) {
                categoryAdapter.setNewData(categoryList);
            } else {
                categoryAdapter = new CategoriesRecyclerViewAdapter(
                        categoryList,
                        requestListener
                );
                GridLayoutManager mLayoutManager = new GridLayoutManager(
                        CookingTimeApp.getAppContext(),
                        2
                );

                categoryRecycler.setLayoutManager(mLayoutManager);
                final LinearLayoutManager layoutManager = (LinearLayoutManager) categoryRecycler.getLayoutManager();

//                categoryRecycler.addItemDecoration(
//                        new RecycleViewItemDecoration(
//                                CookingTimeApp.getAppContext(),
//                                LinearLayoutManager.VERTICAL
//                        )
//                );
                categoryRecycler.setAdapter(categoryAdapter);

                categoryRecycler.addOnItemTouchListener(
                        new RecyclerViewTouchListener(
                                CookingTimeApp.getAppContext(),
                                categoryRecycler,
                                new RecyclerViewTouchListener.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        requestListener.startSearchDishFragment(
                                                Constants.CATEGORIES_TYPE,
                                                categoryAdapter.getCategory(position).getId(),
                                                categoryAdapter.getCategory(position).getName());
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

    private void getCategories() {
        getCalls().getAllCategory().enqueue(new Callback<CategoriesModel>() {
            @Override
            public void onResponse(Call<CategoriesModel> call, Response<CategoriesModel> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToRefreshLayout.refreshComplete();
                    }
                });
                CategoriesModel c = response.body();
                List<Category> categories = c.getCategories();
                if (categories != null) {
                    cacheManager.putOrUpdateCache(TAG, categories);
                    initRecyclerView(categories);
                } else {
                    cacheManager.putOrUpdateCache(TAG, categories);
                }
            }

            @Override
            public void onFailure(Call<CategoriesModel> call, Throwable t) {
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
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
