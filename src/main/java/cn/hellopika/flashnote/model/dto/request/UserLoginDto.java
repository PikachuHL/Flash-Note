package cn.hellopika.flashnote.model.dto.request;

import lombok.Data;

/**
 * @date: 2021/6/16
 * @author: pikachu
 * @description: 用户登录时使用的 dto
 **/

@Data
public class UserLoginDto {

    private String phone;
    private String password;
    private String device;
}
