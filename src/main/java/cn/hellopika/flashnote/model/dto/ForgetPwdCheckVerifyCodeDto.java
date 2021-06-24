package cn.hellopika.flashnote.model.dto;

import lombok.Data;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: TODO 类描述
 **/
@Data
public class ForgetPwdCheckVerifyCodeDto {

    private String token;
    private String verifyCode;
}
