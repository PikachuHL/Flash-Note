package cn.hellopika.flashnote.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @date: 2021/7/2
 * @author: pikachu
 * @description: 图片表的实体类
 **/
@Data
public class Image {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String memoId;
    private String name;
    private String imgKey;
    private String url;

}
