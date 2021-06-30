package cn.hellopika.flashnote.model.dto.request;

import lombok.Data;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 创建笔记使用的 dto
 **/
@Data
public class CreateMemoDto {
    private String content;
    private String parentId;
    private String device;
}
