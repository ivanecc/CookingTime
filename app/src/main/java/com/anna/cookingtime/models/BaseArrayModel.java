package com.anna.cookingtime.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iva on 18.03.17.
 */

public class BaseArrayModel<Model> {
    private int nextPage;
    private List<Model> data;

    public int getNextPage() {
        return nextPage;
    }

    public List<Model> getData() {
        return data;
    }
}
