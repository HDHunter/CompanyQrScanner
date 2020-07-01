package com.hunter.appinfomonitor.network.okbiz;

import com.hunter.appinfomonitor.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * retrofit网络请求请求类
 *
 * @author Hunter
 * Created on 22/03/2017.
 */
public class RetrofitHttpClient {

    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;
    private Retrofit mRetrofit = null;


    private static class SingletonHolder {
        static RetrofitHttpClient INSTANCE = new RetrofitHttpClient();
    }

    public static RetrofitHttpClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitHttpClient() {
        OkHttpClient.Builder clientBuilder = getClientBuilder();
        mRetrofit = getRetrofit(clientBuilder);
    }

    protected Retrofit getRetrofit() {
        return mRetrofit;
    }

    public Retrofit newRetrofit() {
        OkHttpClient.Builder builder = getClientBuilder();
        return getRetrofit(builder);
    }

    private OkHttpClient.Builder getClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        if (LogUtils.isLog()) {
            HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtils.e("ChopeHttpClient", message);
                }
            };
            HttpLoggingInterceptor log = new HttpLoggingInterceptor(logger);
            builder.addNetworkInterceptor(log.setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        builder.addInterceptor(new AllInterceptor());
        return builder;
    }

    private Retrofit getRetrofit(OkHttpClient.Builder builder) {
        return new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://121.196.123.1:7001")
                .build();
    }

}
