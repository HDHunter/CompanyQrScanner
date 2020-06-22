package com.hunter.appinfomonitor.network.okbiz;

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
        builder.addHeader("Accept", "application/json;charset=UTF-8")
                .addHeader("Cookie", "")
                //1、channelId     : 渠道id
                .addHeader("channelId", "")
                //2、version       : app版本
                .addHeader("appVersion", "")
                //3、osVersion     : 系统版本
                .addHeader("osVersion", "")
                //6、uuid          : 设备唯一识别码
                .addHeader("uuid", "")
                // 9、source       : app来源"iOS"还是"android"
                .addHeader("platform", "android")
                //10、clientCode   : 固定字符"cinstms"
                .addHeader("clientCode", "")
                // 13 移除系统的ua
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "")
                .method(request.method(), request.body());
        request = builder.build();
        return chain.proceed(request);
    }

}
