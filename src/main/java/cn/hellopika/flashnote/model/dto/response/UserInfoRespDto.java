package cn.hellopika.flashnote.model.dto.response;

import lombok.Data;

/**
 * @date: 2021/6/27
 * @author: pikachu
 * @description: 返回用户详细信息使用的 dto
 **/

@Data
public class UserInfoRespDto {
    private String userId;
    private String nickName;
    private int status;
    private String joinDate;
    private int joinDays;

    private int tagNums;
    private int memoNums;
}
