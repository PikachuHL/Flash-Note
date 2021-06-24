package cn.hellopika.flashnote.model.dto;

import lombok.Data;

/**
 * @date: 2021/6/16
 * @author: pikachu
 * @description: TODO 类描述
 **/

@Data
public class UserLoginDto {

    private String phone;
    private String password;
    private String device;
}
