package com.anna.cookingtime.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anna.cookingtime.CookingTimeApp;
import com.anna.cookingtime.R;
import com.anna.cookingtime.adapters.DishRecyclerViewAdapter;
import com.anna.cookingtime.interfaces.RecyclerViewTouchListener;
import com.anna.cookingtime.models.BaseArrayModel;
import com.anna.cookingtime.models.Dish;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iva on 18.02.17.
 */

public class SearchNameFragment extends BaseFragment {
    public static final String TAG = "SearchName";
    private static final byte DISHES_LIMIT = 15;

    @BindView(R.id.dishesRecycler)
    RecyclerView dishesRecycler;
    @BindView(R.id.swipeToRefreshLayout)
    PtrFrameLayout swipeToRefreshLayout;

    private List<Dish> dishList;
    private byte page = 1;
    private byte nextPage = 1;
    private DishRecyclerViewAdapter dishAdapter;

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

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Dish> dishList = (List<Dish>) cacheManager.getObjectFromCache(TAG);
        if (dishList != null) {
            initRecyclerView(dishList);
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
                return false;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                getDishes();
            }
        });
    }

    private void initRecyclerView(List<Dish> dishList) {
        if (dishList != null) {
            this.dishList = dishList;
            if (dishAdapter != null && dishesRecycler.getAdapter() != null) {
                dishAdapter.setNewData(dishList);
            } else {
                dishAdapter = new DishRecyclerViewAdapter(
                        dishList,
                        requestListener,
                        getCalls()
                );
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                        CookingTimeApp.getAppContext()
                );

                dishesRecycler.setLayoutManager(mLayoutManager);
                final LinearLayoutManager layoutManager = (LinearLayoutManager) dishesRecycler.getLayoutManager();

//                dishesRecycler.addItemDecoration(
//                        new RecycleViewItemDecoration(
//                                CookingTimeApp.getAppContext(),
//                                LinearLayoutManager.VERTICAL
//                        )
//                );
                dishesRecycler.setAdapter(dishAdapter);

                dishesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int lastShowPost = layoutManager.findLastVisibleItemPosition();

                        if ((lastShowPost + 1) == (DISHES_LIMIT * SearchNameFragment.this.page) && SearchNameFragment.this.nextPage != SearchNameFragment.this.page) {
                            page++;
                            getDishes();
                        }

                    }
                });
                dishesRecycler.addOnItemTouchListener(
                        new RecyclerViewTouchListener(
                                CookingTimeApp.getAppContext(),
                                dishesRecycler,
                                new RecyclerViewTouchListener.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        requestListener.startDish(dishAdapter.getDish(position));
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

    private void getDishes() {
        getCalls().getAllDish(page).enqueue(new Callback<BaseArrayModel<Dish>>() {
            @Override
            public void onResponse(Call<BaseArrayModel<Dish>> call, Response<BaseArrayModel<Dish>> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToRefreshLayout.refreshComplete();
                    }
                });
                BaseArrayModel<Dish> dishes = response.body();
                if (dishes != null) {
                    if (!dishes.getData().isEmpty()) {
                        if (page == 1) {
                            cacheManager.putOrUpdateCache(TAG, dishes.getData());
                        } else {
                            List<Dish> federal = (List<Dish>) cacheManager.getObjectFromCache(TAG);
                            federal.addAll(dishes.getData());
                            cacheManager.putOrUpdateCache(TAG, federal);
                        }
                        initRecyclerView(dishes.getData());
                    } else {
                        cacheManager.putOrUpdateCache(TAG, dishes.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseArrayModel<Dish>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeToRefreshLayout.refreshComplete();
                    }
                });
            }
        });
    }
}
