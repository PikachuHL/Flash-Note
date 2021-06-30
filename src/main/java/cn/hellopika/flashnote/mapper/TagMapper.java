package cn.hellopika.flashnote.mapper;

import cn.hellopika.flashnote.model.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @date: 2021/6/28
 * @author: pikachu
 * @description: 标签 mapper
 **/
@Repository
public interface TagMapper extends BaseMapper<Tag> {
}
