package cn.hellopika.flashnote.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hellopika.flashnote.exception.ServiceException;
import cn.hellopika.flashnote.model.dto.ForgetPwdCheckUserDto;
import cn.hellopika.flashnote.model.dto.ForgetPwdCheckVerifyCodeDto;
import cn.hellopika.flashnote.service.ForgetPwdService;
import cn.hellopika.flashnote.util.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: TODO 类描述
 **/

@RestController
@RequestMapping("/forget")
@Slf4j
public class ForgetPwdController {

    @Autowired
    private ForgetPwdService forgetPwdService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 验证用户是否存在
     * @param forgetPwdCheckUserDto
     * @return
     */
    @PostMapping("/checkUser")
    public ApiResult checkUser(@RequestBody ForgetPwdCheckUserDto forgetPwdCheckUserDto){

        forgetPwdService.checkUser(forgetPwdCheckUserDto);
        return ApiResult.success();
    }

    /**
     * 验证短信验证码
     * @param forgetPwdCheckVerifyCodeDto
     * @return
     */
    @PostMapping("/checkVerifyCode")
    public ApiResult checkVerifyCode(@RequestBody ForgetPwdCheckVerifyCodeDto forgetPwdCheckVerifyCodeDto){
        // 判断验证码是否正确
        RBucket<String> verifyCodeCache = redissonClient.getBucket("sms:verify:" + forgetPwdCheckVerifyCodeDto.getToken());
        if (!StringUtils.equals(forgetPwdCheckVerifyCodeDto.getVerifyCode(), verifyCodeCache.get())) {
            throw new ServiceException("验证码错误");
        }else{
            verifyCodeCache.delete();
        }
        return ApiResult.success(forgetPwdCheckVerifyCodeDto.getToken());
    }
}
