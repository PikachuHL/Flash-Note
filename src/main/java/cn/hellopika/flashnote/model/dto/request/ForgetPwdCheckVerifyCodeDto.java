package cn.hellopika.flashnote.model.dto.request;

import lombok.Data;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: 忘记密码功能：验证短信验证码的dto
 **/
@Data
public class ForgetPwdCheckVerifyCodeDto {

    private String token;
    private String verifyCode;
}
