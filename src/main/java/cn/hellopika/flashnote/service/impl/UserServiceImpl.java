package cn.hellopika.flashnote.service.impl;

import cn.hellopika.flashnote.exception.ServiceException;
import cn.hellopika.flashnote.mapper.UserMapper;
import cn.hellopika.flashnote.model.dto.*;
import cn.hellopika.flashnote.model.entity.User;
import cn.hellopika.flashnote.service.UserService;
import cn.hellopika.flashnote.util.DateTimeUtils;
import cn.hellopika.flashnote.util.SysConst;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
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
     * @param dto
     */
    @Override
    public void userRegister(UserRegisterDto dto) {
        // 1. 判断验证码是否正确
        RBucket<String> verifyCodeCache = redissonClient.getBucket(SysConst.RedisPrefix.VERIFY_CODE + dto.getPhone());
        if (!StringUtils.equals(dto.getVerifyCode(), verifyCodeCache.get())) {
            throw new ServiceException("验证码错误");
        } else {
            verifyCodeCache.delete();
        }

        // 2. 判断手机号是否已注册
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", dto.getPhone()));
        if (Objects.nonNull(user)) {
            throw new ServiceException("手机号已注册");
        }

        // 3. 注册用户
        user = new User();
        user.setPhone(dto.getPhone());
        user.setNickName(dto.getPhone());  // 默认昵称为手机号码
        user.setStatus(User.STATUS_NORMAL);
        user.setCreateTime(DateTimeUtils.getNowString());
        user.setPassword(DigestUtils.md5Hex(SysConst.USER_PASSWORD_SALT + dto.getPassword()));

        log.info("用户 {} 注册成功", dto.getPhone());

        userMapper.insert(user);
    }

    /**
     * 用户登录
     *
     * @param dto
     * @return
     */
    @Override
    public User login(UserLoginDto dto) {
        // 判断用户是否存在
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", dto.getPhone()));
        if (user == null) {
            log.info("用户 {} 不存在", dto.getPhone());
            throw new ServiceException("用户不存在，请先注册");
        }

        // 如果5分钟内输错5次密码，账号锁定20分钟
        // 使用redis来限定20分钟
        RBucket<String> loginLock = redissonClient.getBucket(SysConst.RedisPrefix.LOGIN_LOCK + dto.getPhone());
        if (loginLock.isExists()) {
            // 剩余过期时间（milliseconds）
            long expireTime = loginLock.remainTimeToLive();
            throw new ServiceException("密码错误次数过多，请" + (int) (Math.ceil(expireTime / 60000)) + "分钟后再试");
        }

        // 判断密码是否正确
        if (!StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(SysConst.USER_PASSWORD_SALT + dto.getPassword()))) {
            log.info("用户 {} 密码错误", dto.getPhone());

            // 用redis键值对来控制5分钟内输错5次
            RBucket<Integer> errorCount = redissonClient.getBucket(SysConst.RedisPrefix.LOGIN_PASSWORD_ERROR + dto.getPhone());
            if (!errorCount.isExists()) {
                errorCount.set(4, 5, TimeUnit.MINUTES);
                throw new ServiceException("密码错误，还有4次机会");
            }

            int lastCount = errorCount.get() - 1;
            errorCount.set(lastCount, errorCount.remainTimeToLive(), TimeUnit.MILLISECONDS);

            if (lastCount == 0) {
                loginLock.set(dto.getPhone(), 20, TimeUnit.MINUTES);
                throw new ServiceException("密码错误5次，账户锁定20分钟");
            } else {
                log.info("{}毫秒内还有{}次机会", errorCount.remainTimeToLive(), lastCount);
                throw new ServiceException("密码错误，还有" + lastCount + "次机会");
            }
        }

        log.info("用户 {} 成功登录", dto.getPhone());


        return user;
    }

    /**
     * 忘记密码：验证用户是否存在
     * @param dto
     */
    @Override
    public String checkUser(ForgetPwdCheckUserDto dto) {

        // 检查图片验证码是否正确
        RBucket<String> imageCaptchaText = redissonClient.getBucket(SysConst.RedisPrefix.IMAGE_CAPTCHA_TEXT + dto.getCaptchaToken());
        if(!StringUtils.equalsIgnoreCase(dto.getImageCaptchaText(),imageCaptchaText.get())){
            throw new ServiceException("验证码错误");
        }else{
            // 验证码是一次性的
            imageCaptchaText.delete();
        }

        // 检查用户是否存在
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", dto.getPhone()));
        if (user == null){
            throw new ServiceException("用户不存在");
        }

        // 如果用户存在且图片验证码正确，返回给用户一个 token
        // 用于发送短信验证码的时候验证这个请求是从修改密码的第一步过来的
        String token = UUID.randomUUID().toString();
        RBucket<String> rBucket = redissonClient.getBucket(SysConst.RedisPrefix.FORGET_PASSWORD_SEND_VERIFY_CODE + token);
        rBucket.set(dto.getPhone());

        log.info("{} 计划修改密码", dto.getPhone());

        return token;
    }

    /**
     * 忘记密码：验证短信验证码
     * @param dto
     * @return
     */
    @Override
    public String checkVerifyCode(ForgetPwdCheckVerifyCodeDto dto) {
        // 判断验证码是否正确
        RBucket<String> verifyCodeCache = redissonClient.getBucket(SysConst.RedisPrefix.VERIFY_CODE + dto.getToken());
        if (!StringUtils.equals(dto.getVerifyCode(), verifyCodeCache.get())) {
            throw new ServiceException("验证码错误");
        }else{
            verifyCodeCache.delete();
        }

        // 返回一个新的 token，在 设置新密码时使用
        String token = UUID.randomUUID().toString();
        RBucket<String> resetPwdToken = redissonClient.getBucket(SysConst.RedisPrefix.FORGET_PASSWORD_RESET + token);
        RBucket<String> sendVerifyCodeToken = redissonClient.getBucket(SysConst.RedisPrefix.FORGET_PASSWORD_SEND_VERIFY_CODE + dto.getToken());
        resetPwdToken.set(sendVerifyCodeToken.get());

        return token;
    }

    /**
     * 忘记密码：重设密码
     * @param dto
     */
    @Override
    public void resetPwd(ForgetPwdResetPwdDto dto) {
        // 校验两次输入的密码是否一致
        if(!StringUtils.equals(dto.getNewPwd(), dto.getRepeatPwd())){
            throw new ServiceException("两次输入密码不一致，请重新输入");
        }

        // 通过 token 拿到手机号
        RBucket<String> resetPwdToken = redissonClient.getBucket(SysConst.RedisPrefix.FORGET_PASSWORD_RESET + dto.getToken());
        String phone = resetPwdToken.get();

        // 更新数据库中的密码
        String newMd5Pwd = DigestUtils.md5Hex(SysConst.USER_PASSWORD_SALT + dto.getNewPwd());
        userMapper.update(null, new UpdateWrapper<User>().eq("phone", phone).set("password", newMd5Pwd));


    }
}
