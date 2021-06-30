package cn.hellopika.flashnote.mapper;

import cn.hellopika.flashnote.model.entity.Memo;
import cn.hellopika.flashnote.model.entity.MemoTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 笔记 标签 中间表 mapper
 **/
@Repository
public interface MemoTagMapper extends BaseMapper<MemoTag> {

}
