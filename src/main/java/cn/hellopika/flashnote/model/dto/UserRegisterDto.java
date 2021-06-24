package cn.hellopika.flashnote.model.dto;

import lombok.Data;

/**
 * @date: 2021/6/13
 * @author: pikachu
 * @description: 用户注册 dto
 **/

@Data
public class UserRegisterDto {
    private String phone;
    private String password;
    private String verifyCode;
}
