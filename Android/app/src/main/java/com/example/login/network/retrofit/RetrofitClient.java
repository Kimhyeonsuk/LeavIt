package com.example.login.network.retrofit;

import com.example.login.ifs.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String Base_URL="http://192.168.56.1:3000/";
    public static RetrofitService retrofitService(){
        return getInstance().create(RetrofitService.class);
    }
    private static Retrofit getInstance(){
        Gson gson =new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
