package com.anna.cookingtime.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.anna.cookingtime.CookingTimeApp;
import com.anna.cookingtime.interfaces.APICalls;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rabbit on 10/24/16.
 */
public class RestAPICommunicator {

    private static final String TAG = "RestAPICommunicator";
    private static RestAPICommunicator instance;


    private Retrofit retrofit;
    private Retrofit retrofitWithoutHeaders;

    public static RestAPICommunicator getInstance() {
        if (instance == null) {
            instance = new RestAPICommunicator();
        }
        return instance;
    }

    public RestAPICommunicator() {
        init();
    }

    private void init() {


        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // set your desired log level
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        /**
         * checks response codes
         */
        Interceptor codeCheckerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                Log.d(TAG, "intercept: response.code() = " + response.code());
                switch (response.code()) {
                    case Constants.UNATORIZED:

                        break;

                    case Constants.MAINTANCE_MODE:
                        new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                Toast.makeText(
                                        CookingTimeApp.getAppContext(),
                                        "Server Error Occurred...",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        };
                        break;

                    case Constants.TO_MANY_REQUESTS:
                        new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                Toast.makeText(
                                        CookingTimeApp.getAppContext(),
                                        "Too many requests...",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        };
                        break;

                }
                return response;
            }
        };

        // Define the interceptor, add authentication headers

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request()
                        .newBuilder()
                        .addHeader(
                                Constants.ACCESS_TOKEN_HEADERS,
                                Utils.getUser().getAccessToken()
                        )
                        .build();
                return chain.proceed(newRequest);
            }
        };


        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(codeCheckerInterceptor)
                                .addInterceptor(headerInterceptor)
                                .addInterceptor(loggingInterceptor)
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .build()
                )
                .build();

        retrofitWithoutHeaders = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(codeCheckerInterceptor)
                                .addInterceptor(loggingInterceptor)
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .build()
                )
                .build();
    }

    public APICalls getCalls() {

        if (Utils.getUser() != null) {
            return retrofit.create(APICalls.class);
        }

        return retrofitWithoutHeaders.create(APICalls.class);
    }
}
