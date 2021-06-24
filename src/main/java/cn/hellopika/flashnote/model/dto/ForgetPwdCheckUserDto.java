package cn.hellopika.flashnote.model.dto;

import lombok.Data;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: TODO 类描述
 **/
@Data
public class ForgetPwdCheckUserDto {

    String captchaToken;
    String phone;
    String imageCaptchaText;
}
