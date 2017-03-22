package com.anna.cookingtime.interfaces;

import com.anna.cookingtime.models.BaseArrayModel;
import com.anna.cookingtime.models.CategoriesModel;
import com.anna.cookingtime.models.Category;
import com.anna.cookingtime.models.Dish;
import com.anna.cookingtime.models.Ingredients;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICalls {

    @GET("dishes")
    Call<BaseArrayModel<Dish>> getAllDish(@Query("page") int page, @Query("order_by") String value);

    @GET("dishes")
    Call<BaseArrayModel<Dish>> getDishByCategories(@Query("page") int page, @Query("by_category") long value);

    @GET("dishes/{dishId}")
    Call<Dish> getDish(@Path("dishId") long dishId);

    @GET("categories")
    Call<CategoriesModel> getAllCategory();

    @GET("ingredients")
    Call<BaseArrayModel<Ingredients>> getAllIngredients(@Query("page") int page);

}
