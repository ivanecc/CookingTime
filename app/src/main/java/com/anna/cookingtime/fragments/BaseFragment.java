package com.anna.cookingtime.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anna.cookingtime.activities.MainActivity;
import com.anna.cookingtime.interfaces.APICalls;
import com.anna.cookingtime.interfaces.FragmentRequestListener;
import com.anna.cookingtime.utils.CacheManager;
import com.anna.cookingtime.utils.RestAPICommunicator;

/**
 * Created by iva on 18.02.17.
 */

public class BaseFragment extends Fragment {

    protected FragmentRequestListener requestListener;
    protected CacheManager cacheManager = CacheManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestListener = (MainActivity)getActivity();
    }

    protected APICalls getCalls() {

        return RestAPICommunicator.getInstance().getCalls();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
