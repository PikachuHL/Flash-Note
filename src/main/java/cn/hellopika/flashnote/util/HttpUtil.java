package cn.hellopika.flashnote.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 与 Http 相关的工具类
 **/
public class HttpUtil {

    /**
     * 使用 OKHttp 调用第三方接口
     * @param url   接口地址
     * @return
     */
    public static String getRequest(String url){

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
