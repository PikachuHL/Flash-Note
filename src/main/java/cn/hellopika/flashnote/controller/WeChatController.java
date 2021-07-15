package cn.hellopika.flashnote.controller;

import cn.hellopika.flashnote.service.WeChatService;
import cn.hellopika.flashnote.util.result.ApiResult;
import com.qiniu.storage.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @date: 2021/7/3
 * @author: pikachu
 * @description: 对接微信使用的 Controller
 **/
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Autowired
    private WeChatService weChatService;

    /**
     * 微信发给服务器的请求都会到这个方法
     * 可能是 get，也可能是 post，所以这里使用 @RequestMapping
     * @param request
     * @return
     */
    @RequestMapping("/callback")
    public String callback(HttpServletRequest request){
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");

        // 校验signatur
        boolean checkResult = weChatService.checkSignature(signature, timestamp, nonce);

        // 如果校验失败，返回错误信息（信息内容任意）
        if (!checkResult){
            return "error";
        }

        // 如果 echostr 不为空，说明是配置的时候发送的请求，直接返回 echostr
        if(StringUtils.isNotEmpty(request.getParameter("echostr"))){
            return request.getParameter("echostr");
        }

        // 处理微信发来的非配置请求

        try {
            // 获取请求体中的内容
            BufferedReader reader = request.getReader();
            char[] data = new char[512];
            int dataSize = 0;
            StringBuilder sb = new StringBuilder();
            if ((dataSize = reader.read(data)) != -1){
                sb.append(data, 0, dataSize);
            }
            // 调用绑定方法执行绑定
            String respXml = weChatService.postCallback(sb.toString());
            return respXml;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
