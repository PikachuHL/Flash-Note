package cn.hellopika.flashnote.model.dto.request;

import lombok.Data;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: 忘记密码：验证用户使用的 dto
 **/
@Data
public class ForgetPwdCheckUserDto {

    private String captchaToken;
    private String phone;
    private String imageCaptchaText;
}
