package cn.hellopika.flashnote.model.dto;

import lombok.Data;

/**
 * @date: 2021/6/13
 * @author: pikachu
 * @description: 获取验证码 dto
 **/

@Data
public class SendVerifyCodeDto {

    private String phone;  // 手机号
    private String sceneCode;  // 发送验证码的场景码
    private String device;  // 设备代号

}
