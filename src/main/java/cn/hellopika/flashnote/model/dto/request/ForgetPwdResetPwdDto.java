package cn.hellopika.flashnote.model.dto.request;

import lombok.Data;

/**
 * @date: 2021/6/27
 * @author: pikachu
 * @description: 忘记密码：设置新密码的 dto
 **/
@Data
public class ForgetPwdResetPwdDto {
    String token;
    String newPwd;
    String repeatPwd;
}
