package com.example.qrcodescanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtils {

    public static final String DEFAULT_BASE_URL = "http://192.168.0.193/api/";

    private static String baseUrl;
    private static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    static {
        baseUrl = DEFAULT_BASE_URL;
    }

    public static void post(String url, String json, Callback callback) {
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }
}