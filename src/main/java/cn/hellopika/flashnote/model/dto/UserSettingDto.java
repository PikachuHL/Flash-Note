package cn.hellopika.flashnote.model.dto;

import lombok.Data;

/**
 * @date: 2021/6/27
 * @author: pikachu
 * @description: 用户设置使用的 dto
 **/

@Data
public class UserSettingDto {
    private String userId;
    private String nickName;
    private String oldPassword;
    private String newPassword;
    private String repeatNewPassword;
}
