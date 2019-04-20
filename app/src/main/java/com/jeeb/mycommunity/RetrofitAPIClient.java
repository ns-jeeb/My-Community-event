package com.jeeb.mycommunity;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIClient {

    public static String LOCAL_BASE_URL = "http://localhost:9000";
    public static String BASE_URL = "https://community-eve.herokuapp.com/";
    private static Retrofit mRetrofit = null;


    public static  Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        if (BuildConfig.DEBUG) {
            // development build
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor
//                    new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request().newBuilder().addH
//                    return null;
//                }
//            }
            )
                    .addNetworkInterceptor(new StethoInterceptor())
                    .connectTimeout(30, TimeUnit.MINUTES).readTimeout(30,TimeUnit.MINUTES)
                    .build();


            mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        } else {
            // production build
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.MINUTES).readTimeout(30,TimeUnit.MINUTES)
                    .build();

            mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).
                            build();
        }

        return mRetrofit;
    }

    public static  Retrofit getClient(final String token){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        if (BuildConfig.DEBUG) {
            // development build
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Authorization","Bearer "+token).build();
                    return chain.proceed(request);
                }
            })
                    .addNetworkInterceptor(new StethoInterceptor())
                    .connectTimeout(30, TimeUnit.MINUTES).readTimeout(30,TimeUnit.MINUTES)
                    .build();

            mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        } else {
            // production build
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.MINUTES).readTimeout(30,TimeUnit.MINUTES)
                    .build();

            mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }

        return mRetrofit;
    }
}
