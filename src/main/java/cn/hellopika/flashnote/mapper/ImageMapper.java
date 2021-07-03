package cn.hellopika.flashnote.mapper;

import cn.hellopika.flashnote.model.entity.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @date: 2021/7/2
 * @author: pikachu
 * @description: 图片功能的 Mapper
 **/
@Repository
public interface ImageMapper extends BaseMapper<Image> {
}
