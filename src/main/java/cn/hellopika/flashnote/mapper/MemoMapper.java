package cn.hellopika.flashnote.mapper;

import cn.hellopika.flashnote.model.entity.Memo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 笔记功能的 mapper
 **/
@Repository
public interface MemoMapper extends BaseMapper<Memo> {
    List<Memo> getMemosByTag(String userId, String tagName);

}
