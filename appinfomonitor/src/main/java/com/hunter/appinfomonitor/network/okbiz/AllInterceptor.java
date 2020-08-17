package com.hunter.appinfomonitor.network.okbiz;

import com.hunter.appinfomonitor.MyApplication;
import com.hunter.appinfomonitor.ui.OtaAPi;

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
        if (request.url().toString().contains(OtaAPi.pabase)) {
            String token = Yodo1SharedPreferences.getString(MyApplication.mApplication, "tokenpa");
            builder.addHeader("Authorization", "Bearer" + " " + token)
                    .method(request.method(), request.body());
        } else {
            String token = Yodo1SharedPreferences.getString(MyApplication.mApplication, "token");
            builder.addHeader("Authorization", "Bearer" + " " + token)
                    .method(request.method(), request.body());
        }
        request = builder.build();
        return chain.proceed(request);
    }

}
