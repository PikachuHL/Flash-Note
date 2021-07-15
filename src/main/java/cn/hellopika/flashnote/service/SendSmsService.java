package cn.hellopika.flashnote.service;

import cn.hellopika.flashnote.model.dto.request.SendVerifyCodeDto;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 发送短信验证码的service层接口
 **/
public interface SendSmsService {
    void sendSms(SendVerifyCodeDto sendVerifyCodeDto);
}
