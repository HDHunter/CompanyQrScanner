package com.hunter.appinfomonitor.network.okbiz;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * common http operation.
 * <p>
 * Created by hdhunter on 17-6-28.
 */
public interface ChopeBaseService {


    @GET
    Observable<String> get(@Url String url);

    @GET
    Observable<String> get(@Url String url, @QueryMap Map<String, String> queryParams);

    @GET
    Observable<String> get(@Url String url, @QueryMap Map<String, String> queryParams, @HeaderMap Map headers);

    @POST
    @FormUrlEncoded
    Observable<String> post(@Url String url);

    @POST
    @FormUrlEncoded
    Observable<String> post(@Url String url, @FieldMap Map<String, String> queryParams);

    @POST
    @FormUrlEncoded
    Observable<String> post(@Url String url, @FieldMap Map<String, String> queryParams, @HeaderMap Map headers);


    @POST
    Observable<String> post(@Url String url, @Body MultipartBody requests);

    @POST
    Observable<String> post(@Url String url, @Body RequestBody requests);
}
