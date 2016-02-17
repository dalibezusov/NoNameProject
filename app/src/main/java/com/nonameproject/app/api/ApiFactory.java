package com.nonameproject.app.api;

import android.support.annotation.NonNull;
import com.squareup.okhttp.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.util.concurrent.TimeUnit;

public class ApiFactory {

    private static final String API_URL = "http://84.237.16.44";


    private static final int TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final int CONNECT_TIMEOUT = 20;

    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {

        CLIENT.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);

    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CLIENT)
                .build();
    }

    @NonNull
    public static PlatypusService getWidgetService(){
        return getRetrofit().create(PlatypusService.class);
    }

}
