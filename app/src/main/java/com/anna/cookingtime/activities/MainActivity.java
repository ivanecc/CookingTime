package com.anna.cookingtime.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.anna.cookingtime.R;
import com.anna.cookingtime.fragments.SearchNameFragment;
import com.anna.cookingtime.fragments.dish.DishFragment;
import com.anna.cookingtime.interfaces.FragmentRequestListener;
import com.anna.cookingtime.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentRequestListener {

    @BindView(R.id.activity_main)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigationView)
    NavigationView navigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initNavigationView();

        startSearchNameFragment();

    }

    private void initNavigationView() {
        navigationView.getMenu().findItem(R.id.navName).setVisible(false);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openDrawer,
                R.string.closeDrawer);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.navName:
                        setHideNamePoint();
                        startSearchNameFragment();
                        return true;
                    case R.id.navIngredients:
                        setHideIngredPoint();

                        return true;
                    case R.id.navGroup:
                        setHideGroupPoint();
                        return true;
                }
                return true;
            }
        });
    }

    private void setHideGroupPoint() {
        navigationView.getMenu().findItem(R.id.navGroup).setVisible(false);
        navigationView.getMenu().findItem(R.id.navIngredients).setVisible(true);
        navigationView.getMenu().findItem(R.id.navName).setVisible(true);
    }

    private void setHideIngredPoint() {
        navigationView.getMenu().findItem(R.id.navIngredients).setVisible(false);
        navigationView.getMenu().findItem(R.id.navGroup).setVisible(true);
        navigationView.getMenu().findItem(R.id.navName).setVisible(true);
    }

    private void setHideNamePoint() {
        navigationView.getMenu().findItem(R.id.navName).setVisible(false);
        navigationView.getMenu().findItem(R.id.navGroup).setVisible(true);
        navigationView.getMenu().findItem(R.id.navIngredients).setVisible(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return actionBarDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if (Utils.isRootFragment()) {
                setTitle(R.string.app_name);
            }
        }
    }

    @Override
    public void startSearchNameFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content,
                        new SearchNameFragment(),
                        SearchNameFragment.TAG)
                .commit();
    }

    @Override
    public void startDish(long id) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content,
                        DishFragment.newInstance(id),
                        DishFragment.TAG)
                .addToBackStack(DishFragment.TAG)
                .commit();
    }

}
