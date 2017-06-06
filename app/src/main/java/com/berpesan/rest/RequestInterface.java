package com.berpesan.rest;

import java.util.concurrent.TimeUnit;

import com.berpesan.model.DataSms;
import com.berpesan.model.JSONResponse;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by itdel on 4/3/17.
 */

public interface RequestInterface {
    //classify the sms
    @GET("/berpesan/classify")
    Call<DataSms> classify(@Query("sms") String sms);

    //trending spam
    @GET("/berpesan/spamtrend")
    Call<JSONResponse> getJSON();

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://103.23.20.171")//replace with Berpesan VM IP
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}
