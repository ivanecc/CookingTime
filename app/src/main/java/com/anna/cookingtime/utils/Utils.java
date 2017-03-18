package com.anna.cookingtime.utils;

import com.anna.cookingtime.models.User;

/**
 * Created by iva on 17.03.17.
 */

public class Utils {

    private static User user;

    public static void setUser(User u) {
        user = u;
    }

    public static User getUser() {
        return user;
    }
}
