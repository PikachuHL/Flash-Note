package cn.hellopika.flashnote.service.impl;

import cn.hellopika.flashnote.exception.ServiceException;
import cn.hellopika.flashnote.mapper.UserMapper;
import cn.hellopika.flashnote.model.dto.UserLoginDto;
import cn.hellopika.flashnote.model.entity.User;
import cn.hellopika.flashnote.model.dto.UserRegisterDto;
import cn.hellopika.flashnote.service.UserService;
import cn.hellopika.flashnote.util.DateTimeUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 用户相关 业务层 实现类
 **/

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 用户注册
     *
     * @param userRegisterDto
     */
    @Override
    public void userRegister(UserRegisterDto userRegisterDto) {
        // 1. 判断验证码是否正确
        RBucket<String> verifyCodeCache = redissonClient.getBucket("sms:verify:" + userRegisterDto.getPhone());
        if (!StringUtils.equals(userRegisterDto.getVerifyCode(), verifyCodeCache.get())) {
            throw new ServiceException("验证码错误");
        }else{
            verifyCodeCache.delete();
        }

        // 2. 判断手机号是否已注册
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", userRegisterDto.getPhone()));
        if (Objects.nonNull(user)) {
            throw new ServiceException("手机号已注册");
        }

        // 3. 注册用户
        user = new User();
        user.setPhone(userRegisterDto.getPhone());
        user.setNickName(userRegisterDto.getPhone());  // 默认昵称为手机号码
        user.setStatus(User.STATUS_NORMAL);
        user.setCreateTime(DateTimeUtils.getNowString());
        user.setPassword(userRegisterDto.getPassword());  // TODO md5加密

        log.info("用户 {} 注册成功", userRegisterDto.getPhone());

        userMapper.insert(user);
    }

    /**
     * 用户登录
     * @param userLoginDto
     * @return
     */
    @Override
    public User login(UserLoginDto userLoginDto) {
        // 判断用户是否存在
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", userLoginDto.getPhone()));
        if(Objects.isNull(user)){
            log.info("用户 {} 不存在", userLoginDto.getPhone());
            throw new ServiceException("用户不存在，请先注册");
        }

        // 如果5分钟内输错5次密码，账号锁定20分钟
        // 使用redis来限定20分钟
        RBucket<String> loginLock = redissonClient.getBucket("login:lock:" + userLoginDto.getPhone());
        if(loginLock.isExists()){
            // 剩余过期时间（milliseconds）
            long expireTime = loginLock.remainTimeToLive();
            throw new ServiceException("密码错误次数过多，请" + (int)(Math.ceil(expireTime/60000)) + "分钟后再试");
        }

        // 判断密码是否正确
        // TODO 使用 MD5 密文比对密码
        if(!StringUtils.equals(user.getPassword(), userLoginDto.getPassword())){
            log.info("用户 {} 密码错误", userLoginDto.getPhone());

            // 用redis键值对来控制5分钟内输错5次
            RBucket<Integer> errorCount = redissonClient.getBucket("login:pwd:error:" + userLoginDto.getPhone());
            if (!errorCount.isExists()){
                errorCount.set(4, 5, TimeUnit.MINUTES);
                throw new ServiceException("密码错误，还有4次机会");
            }

            int lastCount = errorCount.get() - 1;
            errorCount.set(lastCount, errorCount.remainTimeToLive(), TimeUnit.MILLISECONDS);

            if(lastCount == 0){
                loginLock.set(userLoginDto.getPhone(), 20, TimeUnit.MINUTES);
                throw new ServiceException("密码错误5次，账户锁定20分钟");
            }else{
                log.info("{}毫秒内还有{}次机会", errorCount.remainTimeToLive(), lastCount);
                throw new ServiceException("密码错误，还有"+ lastCount +"次机会");
            }
        }

        log.info("用户 {} 成功登录", userLoginDto.getPhone());


        return user;
    }
}
