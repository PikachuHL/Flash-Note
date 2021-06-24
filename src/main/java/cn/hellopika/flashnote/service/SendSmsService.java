package cn.hellopika.flashnote.service;

import cn.hellopika.flashnote.model.dto.SendVerifyCodeDto;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: TODO 类描述
 **/
public interface SendSmsService {
    void sendSms(SendVerifyCodeDto sendVerifyCodeDto);
}
