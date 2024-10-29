package com.example.newmessenger;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Api instance;
    public static final String BASE_URL = "http://currates.ru:8000/";

    private ApiService api;
    private Retrofit retrofit;

    private Api() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);
    }

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }

        return instance;
    }

    public ApiService getApiService() {
        return api;
    }
}
