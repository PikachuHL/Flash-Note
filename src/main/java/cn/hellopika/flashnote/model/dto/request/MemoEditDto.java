package cn.hellopika.flashnote.model.dto.request;

import lombok.Data;

/**
 * @date: 2021/6/29
 * @author: pikachu
 * @description: 编辑笔记内容使用的 dto
 **/
@Data
public class MemoEditDto {
    private String memoId;
    private String content;
    private String userId;
    private String device;
}
