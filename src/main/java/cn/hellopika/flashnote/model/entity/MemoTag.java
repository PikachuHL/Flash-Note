package cn.hellopika.flashnote.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 笔记 用户 中间表的实体类
 **/

@Data
public class MemoTag {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String memoId;
    private String tagId;
}
