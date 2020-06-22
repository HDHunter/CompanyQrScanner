package com.hunter.appinfomonitor.network;

/**
 * Created by michael_mac_pro on 16/7/26.
 */
public interface GuiqiuStringResponseListener {

    /**
     * 请求成功
     */
    void onSuccess(String response);

    /**
     * 请求失败
     */
    void onFailure(Throwable error);

}
