package com.hunter.appinfomonitor.http;

import com.loopj.android.http.AsyncHttpClient;

public class ApiRequest {

    private static AsyncHttpClient client = null;


    public static AsyncHttpClient getClient() {
        if (client == null) {
            client = new AsyncHttpClient();
        }
        client.removeAllHeaders();
        return client;
    }
}
