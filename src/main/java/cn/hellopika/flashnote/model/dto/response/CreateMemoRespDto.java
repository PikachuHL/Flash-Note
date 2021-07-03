package cn.hellopika.flashnote.model.dto.response;

import cn.hellopika.flashnote.model.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 创建笔记后，返回给前端该笔记相关数据的 dto
 **/
@Data
@AllArgsConstructor
public class CreateMemoRespDto {
    String memoId;
    String content;
    String createTime;
    String device;
    String parentId;

    /**
     * 笔记的标签
     */
    List<String> tags;

    /**
     * 笔记中的图片
     */
    List<Image> images;
}
