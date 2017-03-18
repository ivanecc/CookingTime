package com.anna.cookingtime;

import android.app.Application;
import android.content.Context;

/**
 * Created by iva on 17.03.17.
 */

public class CookingTimeApp extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }


    public static Context getAppContext() {
        return sContext;
    }

}
