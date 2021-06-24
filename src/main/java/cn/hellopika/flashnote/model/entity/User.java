package cn.hellopika.flashnote.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: TODO 类描述
 **/

@Data
public class User implements Serializable {

    /**
     * 用户状态
     */
    public static final int STATUS_NORMAL = 0;  // 正常
    public static final int STATUS_BAN = 1;  // 禁用


    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String phone;
    private String nickName;
    private String password;
    private String createTime;
    private int status;
}
