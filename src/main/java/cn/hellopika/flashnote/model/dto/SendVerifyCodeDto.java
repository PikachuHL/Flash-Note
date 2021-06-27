package cn.hellopika.flashnote.model.dto;

import lombok.Data;

/**
 * @date: 2021/6/13
 * @author: pikachu
 * @description: 获取验证码 dto
 **/

@Data
public class SendVerifyCodeDto {

    // 用户注册场景值
    public static final String SEND_VERIFYCODE_REGISTER = "1001";

    // 修改密码场景值
    public static final String SEND_VERIFYCODE_FORGETPWD = "1002";

    private String phone;  // 手机号
    private String sceneCode;  // 发送验证码的场景码
    private String device;  // 设备代号

}
