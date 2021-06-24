package cn.hellopika.flashnote.mapper;

import cn.hellopika.flashnote.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 用户 mapper
 **/

@Repository
public interface UserMapper extends BaseMapper<User> {

}
