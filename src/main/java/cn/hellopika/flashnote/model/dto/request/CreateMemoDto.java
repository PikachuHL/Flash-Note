package cn.hellopika.flashnote.model.dto.request;

import cn.hellopika.flashnote.model.entity.Image;
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
    /**
     * 笔记中的图片
     */
    private Image[] images;
}
