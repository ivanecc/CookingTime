package com.anna.cookingtime.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.anna.cookingtime.CookingTimeApp;
import com.anna.cookingtime.R;
import com.anna.cookingtime.activities.MainActivity;
import com.anna.cookingtime.adapters.DishRecyclerViewAdapter;
import com.anna.cookingtime.interfaces.RecyclerViewTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iva on 23.03.17.
 */

public class FavoritesFragment extends BaseFragment {
    public static final String TAG = "FavoritesFragment";
    @BindView(R.id.dishesRecycler)
    RecyclerView dishesRecycler;

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
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_favorite));
        initRecyclerView();
    }

    private void initRecyclerView() {
        dishAdapter = new DishRecyclerViewAdapter(
                cacheManager.getFavoritesList(),
                requestListener,
                getCalls()
        );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                CookingTimeApp.getAppContext()
        );

        dishesRecycler.setLayoutManager(mLayoutManager);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) dishesRecycler.getLayoutManager();

//                categoryRecycler.addItemDecoration(
//                        new RecycleViewItemDecoration(
//                                CookingTimeApp.getAppContext(),
//                                LinearLayoutManager.VERTICAL
//                        )
//                );
        dishesRecycler.setAdapter(dishAdapter);

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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
