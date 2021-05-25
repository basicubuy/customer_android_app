/*
 * Copyright (c) 2021. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.ubuyapi.retrofit;

import com.ubuyng.app.Services.TaskService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit getRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ubuy.ng/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static TaskService getTaskService(){
        TaskService taskService = getRetrofit().create(TaskService.class);
        return taskService;
    }
}

