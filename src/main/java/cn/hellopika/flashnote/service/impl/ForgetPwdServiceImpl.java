package cn.hellopika.flashnote.service.impl;

import cn.hellopika.flashnote.exception.ServiceException;
import cn.hellopika.flashnote.mapper.UserMapper;
import cn.hellopika.flashnote.model.dto.ForgetPwdCheckUserDto;
import cn.hellopika.flashnote.model.entity.User;
import cn.hellopika.flashnote.service.ForgetPwdService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.util.Objects;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: TODO 类描述
 **/

@Service
@Slf4j
public class ForgetPwdServiceImpl implements ForgetPwdService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void checkUser(ForgetPwdCheckUserDto forgetPwdCheckUserDto) {
        // 检查用户是否存在
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", forgetPwdCheckUserDto.getPhone()));
        if (Objects.isNull(user)){
            throw new ServiceException("用户不存在");
        }

        // 检查验证码是否正确
        RBucket<String> imageCaptchaText = redissonClient.getBucket("imageCaptcha:text:" + forgetPwdCheckUserDto.getCaptchaToken());
        if(!StringUtils.equals(forgetPwdCheckUserDto.getImageCaptchaText(),imageCaptchaText.get())){
            throw new ServiceException("验证码错误");
        }else{
            // 验证码是一次性的
            imageCaptchaText.delete();
        }

        // TODO 如果用户存在且验证码正确，返回给用户一个 token


        log.info("{} 计划修改密码", forgetPwdCheckUserDto.getPhone());

    }
}
