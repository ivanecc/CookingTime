package com.anna.cookingtime.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anna.cookingtime.activities.MainActivity;
import com.anna.cookingtime.interfaces.FragmentRequestListener;

import io.realm.Realm;

/**
 * Created by iva on 18.02.17.
 */

public class BaseFragment extends Fragment {

    protected FragmentRequestListener requestListener;
    protected Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestListener = (MainActivity)getActivity();
        realm = Realm.getDefaultInstance();
    }

//    protected List<Photo> readFromDB() {
//        List<Photo> list = new ArrayList<>();
//        RealmResults<Photo> results = realm.where(Photo.class).findAll();
//        for (Photo e : results) {
//            e.convertImageUri();
//            list.add(e);
//        }
//        return list;
//    }
//
//    protected void addToDB(Photo photo) {
//        realm.beginTransaction();
//        realm.copyToRealm(photo);
//        realm.commitTransaction();
//    }
//
//    protected void removeFromRealm(long id) {
//        realm.beginTransaction();
//        Photo result = realm.where(Photo.class).equalTo(Constants.ID, id).findFirst();
//        result.convertImageUri();
//        Log.d(TAG, "isDelete - " + new File(result.getImageUri().getPath()).delete());
//        result.deleteFromRealm();
//        realm.commitTransaction();
//    }
//
//    protected Photo getPhoto(long id) {
//        Photo result = realm.where(Photo.class).equalTo(Constants.ID, id).findFirst();
//        result.convertImageUri();
//        return result;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
