package cn.hellopika.flashnote.util;

import okhttp3.*;

import java.io.IOException;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 与 Http 相关的工具类
 **/
public class HttpUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 使用 OKHttp 发送 get 请求
     * @param url   接口地址
     * @return
     */
    public static String sendGet(String url){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用 OKHttp 发送 post 请求
     * @param url   请求地址
     * @param json  请求参数
     * @return
     */
    public static String sendPost(String url, String json){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
