package com.synechron.synehire.ws;

import android.content.Context;

import com.synechron.synehire.utility.ConnectivityInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "http://172.22.204.134:8080";
    private static APIService apiService;
    private static Retrofit retrofit = null;

    private APIClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        apiService = retrofit.create(APIService.class);
    }

    public static APIService getInstance(Context context) {
        if (apiService == null) {
            synchronized (APIClient.class) {
                if (apiService == null) {
                   new APIClient(context);
                }
            }
        }
        return apiService;
    }
}
