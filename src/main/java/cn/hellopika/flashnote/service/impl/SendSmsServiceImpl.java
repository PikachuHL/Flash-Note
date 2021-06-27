package cn.hellopika.flashnote.service.impl;

import cn.hellopika.flashnote.model.dto.SendSmsDto;
import cn.hellopika.flashnote.model.dto.SendVerifyCodeDto;
import cn.hellopika.flashnote.service.SendSmsService;
import cn.hellopika.flashnote.util.SysConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: TODO 类描述
 **/
@Service
@Slf4j
public class SendSmsServiceImpl implements SendSmsService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendSms(SendVerifyCodeDto sendVerifyCodeDto) {
        // 60秒内只能发一次验证码
        RBucket<String> verifyCodeSendCache = redissonClient.getBucket(SysConst.RedisPrefix.SEND_VERIFY_CODE + sendVerifyCodeDto.getPhone());
        // 验证码有效期 10分钟
        RBucket<String> verifyCodeCache = redissonClient.getBucket(SysConst.RedisPrefix.VERIFY_CODE + sendVerifyCodeDto.getPhone());

        // 如果 verifyCodeSendCache 存在，证明 60秒内发送过验证码，直接抛异常
        if(verifyCodeSendCache.isExists()){
            log.error("{} 频繁发送验证码，已被拒绝", sendVerifyCodeDto.getPhone());
            throw new RuntimeException("请不要频繁发送验证码");
        }

        // 获取验证码：随机6位数字
        String verifyCode = RandomStringUtils.randomNumeric(6);

        // 把验证码放到缓存中
        verifyCodeSendCache.set(verifyCode, 60, TimeUnit.SECONDS);
        verifyCodeCache.set(verifyCode, 10, TimeUnit.MINUTES);

        // 根据场景值的不同，调用不同的方法发送短信验证码
        if(StringUtils.equals(sendVerifyCodeDto.getSceneCode(), SendVerifyCodeDto.SEND_VERIFYCODE_REGISTER)){
            registerSendSms(verifyCode, sendVerifyCodeDto.getPhone());
        }else if(StringUtils.equals(sendVerifyCodeDto.getSceneCode(), SendVerifyCodeDto.SEND_VERIFYCODE_FORGETPWD)){
            forgetPwdSendSms(verifyCode, sendVerifyCodeDto.getPhone());
        }

        log.info(verifyCode);
    }

    /**
     * 用户注册时发送验证码
     * @param verifyCode
     * @param phone
     */
    private void registerSendSms(String verifyCode, String phone){
        String message = "【皮卡丘】您正在注册闪念笔记，短信验证码为：" + verifyCode + "，短信有效时间为10分钟。";
        // 把发短信的相关信息放到消息队列中
        rabbitTemplate.convertAndSend("sms_quene", new SendSmsDto(phone, message));

    }

    /**
     * 忘记密码时发送验证码
     * @param verifyCode
     * @param token
     */
    private void forgetPwdSendSms(String verifyCode, String token){
        RBucket<String> rBucket = redissonClient.getBucket(SysConst.RedisPrefix.FORGET_PASSWORD_SEND_VERIFY_CODE + token);

        String message = "【皮卡丘】您正在修改密码，短信验证码为：" + verifyCode + "，短信有效时间为10分钟。";
        // 把发短信的相关信息放到消息队列中
        rabbitTemplate.convertAndSend("sms_quene", new SendSmsDto(rBucket.get(), message));

    }

}
