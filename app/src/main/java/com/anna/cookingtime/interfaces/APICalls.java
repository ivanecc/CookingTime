package com.anna.cookingtime.interfaces;

import com.anna.cookingtime.models.BaseArrayModel;
import com.anna.cookingtime.models.Dish;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICalls {

    @GET("dishes.json")
    Call<BaseArrayModel<Dish>> getAllDish(@Query("page") int page);

    @GET("dishes/{dishId}.json")
    Call<Dish> getDish(@Path("dishId") long dishId);

}
