package com.hunter.appinfomonitor.network.okbiz;

import com.hunter.appinfomonitor.MyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 30天过期登录
 *
 * @author duanshoucheng
 */
public class AllInterceptor implements Interceptor {

    public static String RefreshToken = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder builder = request.newBuilder();
        // 添加PPU;
        String token = Yodo1SharedPreferences.getString(MyApplication.mApplication, "token");
        builder.addHeader("Authorization", "Bearer" + " " + token)
                .method(request.method(), request.body());
        request = builder.build();
        return chain.proceed(request);
    }

}
