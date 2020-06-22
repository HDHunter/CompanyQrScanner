package com.hunter.appinfomonitor.network.okbiz;


import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hunter.appinfomonitor.network.okbizfile.FileInfo;
import com.hunter.appinfomonitor.network.okbizfile.FileUtils;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * Base Common Api.
 * <p>
 * String get/post
 * JSONObject get/post
 * <p>
 * Created by hdhunter on 17-6-28.
 */
public class GunqiuApi {

    private volatile static GunqiuApi INSTANCE;

    private GunqiuApi() {
        super();
    }

    public static GunqiuApi getInstance() {
        if (INSTANCE == null) {
            synchronized (GunqiuApi.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GunqiuApi();
                }
            }
        }
        return INSTANCE;
    }

    public <T> T getApi(Class<T> clazz) {
        return RetrofitHttpClient.getInstance().getRetrofit().create(clazz);
    }

    private ChopeBaseService getBase() {
        return RetrofitHttpClient.getInstance().getRetrofit().create(ChopeBaseService.class);
    }

    public Observable<String> get(String url, Map<String, String> queryParams, Map headers) {
        return getBase().get(url, queryParams, headers);
    }

    public Observable<String> get(String url, Map<String, String> queryParams) {
        return getBase().get(url, queryParams);
    }

    public Observable<String> get(String url) {
        return getBase().get(url);
    }

    public Observable<String> post(String url, Map<String, String> queryParams, Map headers) {
        return getBase().post(url, queryParams, headers);
    }

    /**
     * url encode 编码请求
     */
    public Observable<String> post(String url, Map<String, String> queryParams) {
        return getBase().post(url, queryParams);
    }

    public Observable<String> post(String url) {
        return getBase().post(url);
    }

    /**
     * Json body请求。
     */
    public Observable<String> post(String url, String postBody) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postBody);
        return getBase().post(url, requestBody);
    }

    /**
     * multitype,upload File.
     * upload Map<String,String> or File.many keyValue and one file.
     */
    public Observable<String> postFile(String url, @Nullable Map<String, String> queryParams, @Nullable FileInfo fileInfo) {
        return postFiles(url, queryParams, new FileInfo[]{fileInfo});
    }

    /**
     * 多字段,多文件上传.FileInfo->fileName,file为必须上传...到这里,才是万能post请求.
     * <p>
     * upload Map<String,String> or File.many keyValue and one file.
     */
    public Observable<String> postFiles(String url, @Nullable Map<String, String> queryParams, @Nullable FileInfo[] fileInfos) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (queryParams != null) {
            for (Map.Entry<String, String> value : queryParams.entrySet()) {
                builder.addFormDataPart(value.getKey(), value.getValue());
            }
        }
        if (fileInfos != null && fileInfos.length > 0 && fileInfos[0].file != null) {
            for (int i = 0; i < fileInfos.length; i++) {
                String fileType = !TextUtils.isEmpty(fileInfos[i].fileType) ? fileInfos[i].fileType : FileUtils.getMimeType(fileInfos[i].file);
                String fileName = !TextUtils.isEmpty(fileInfos[i].fileName) ? fileInfos[i].fileName : fileInfos[i].file.getName();
                RequestBody requestBody = MultipartBody.create(MediaType.parse(fileType), fileInfos[i].file);
                MultipartBody.Part fileMultipart = MultipartBody.Part.createFormData(fileName, fileName, requestBody);
                builder.addPart(fileMultipart);
            }
            return getBase().post(url, builder.build());
        } else if (queryParams != null) {
            return getBase().post(url, queryParams);
        } else {
            return getBase().post(url);
        }
    }

    //原始的请求，没有任何应用层的head，等添加
    private Retrofit retrofit;

    public ChopeBaseService getRaw() {
        return getRaw(ChopeBaseService.class);
    }

    public <T> T getRaw(Class<T> clazz) {
        if (retrofit == null) {
            retrofit = RetrofitHttpClient.getInstance().newRetrofit();
        }
        return retrofit.create(clazz);
    }

    public Observable<String> getRaw(String url, Map<String, String> queryParams) {
        return getRaw().get(url, queryParams);
    }
}
