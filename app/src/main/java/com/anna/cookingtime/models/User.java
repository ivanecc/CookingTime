package com.anna.cookingtime.models;

/**
 * Created by iva on 17.03.17.
 */

public class User {
    private long id;
    private String name;
    private transient String accessToken;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
