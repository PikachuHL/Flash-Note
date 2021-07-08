package cn.hellopika.flashnote.util.api;


import cn.hellopika.flashnote.config.SmsConfig;
import cn.hellopika.flashnote.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @date: 2021/6/13
 * @author: pikachu
 * @description: 发送短信 API
 **/

@Component
@Slf4j
public class SmsApi {

    @Autowired
    private SmsConfig smsConfig;

    public void sendSms(String phone, String message) {

        message = encodeUtf(message);  // 短信宝要求字符编码为 UTF-8

        String url = "http://api.smsbao.com/sms?u=" + smsConfig.getUsername() + "&p=" + smsConfig.getPassword() + "&m=" + phone + "&c=" + message;
        String result = HttpUtils.sendGet(url);
        log.info(result);
    }

    /**
     * 把字符串编码改为 UTF-8 格式
     *
     * @param str
     * @return
     */
    private String encodeUtf(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


}
