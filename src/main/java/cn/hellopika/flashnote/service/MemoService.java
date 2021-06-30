package cn.hellopika.flashnote.service;

import cn.hellopika.flashnote.model.dto.request.CreateMemoDto;
import cn.hellopika.flashnote.model.dto.request.MemoEditDto;
import cn.hellopika.flashnote.model.dto.response.CreateMemoRespDto;
import cn.hellopika.flashnote.model.entity.Memo;
import cn.hellopika.flashnote.model.entity.Tag;

import java.util.List;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 笔记功能的 业务层接口
 **/
public interface MemoService {

    /**
     * 创建笔记
     * @param dto
     * @param userId
     */
    CreateMemoRespDto createMemo(CreateMemoDto dto, String userId);

    /**
     * 获取标签列表
     * @param userId
     * @return
     */
    List<String> getTags(String userId);

    /**
     * 获取笔记列表
     * @param UserId
     * @param tagName
     * @return
     */
    List<Memo> getMemos(String UserId, String tagName);

    /**
     * 删除笔记
     * @param userId
     * @param memeId
     */
    void delMemo(String userId, String memeId);

    void editMemo(MemoEditDto dto);
}
