package com.synechrone.synehire.ws;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "http://172.22.204.134:8080";
    //private static final String BASE_URL = "http://100.76.133.196:8080";
    private static APIService apiService;
    private static Retrofit retrofit = null;

    private APIClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        apiService = retrofit.create(APIService.class);
    }

    public static APIService getInstance() {
        if (apiService == null) {
            synchronized (APIClient.class) {
                if (apiService == null) {
                   new APIClient();
                }
            }
        }
        return apiService;
    }
}
