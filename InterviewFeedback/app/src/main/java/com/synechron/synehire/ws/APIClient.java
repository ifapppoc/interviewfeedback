package com.synechron.synehire.ws;

import android.content.Context;

import com.synechron.synehire.constants.AppConstants;
import com.synechron.synehire.utility.BasicAuthInterceptor;
import com.synechron.synehire.utility.ConnectivityInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "https://synehire.synechron.com";
    private static APIService apiService;
    private static Retrofit retrofit = null;

    private APIClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ConnectivityInterceptor(context))
                    .addInterceptor(new BasicAuthInterceptor("synehr", AppConstants.GLOBAL_USER_PASSWORD)).build();

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
