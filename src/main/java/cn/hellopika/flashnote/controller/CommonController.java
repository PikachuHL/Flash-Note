package cn.hellopika.flashnote.controller;

import cn.hellopika.flashnote.model.dto.response.ImageCaptchaRespDto;
import cn.hellopika.flashnote.model.dto.request.SendVerifyCodeDto;
import cn.hellopika.flashnote.service.ImageCaptchaService;
import cn.hellopika.flashnote.service.SendSmsService;
import cn.hellopika.flashnote.util.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @date: 2021/6/10
 * @author: pikachu
 * @description: 各种公用 controller
 **/

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private SendSmsService sendsmsService;

    @Autowired
    private ImageCaptchaService imageCaptchaService;

    /**
     * 发送短信验证码
     * @param sendVerifyCodeDto
     * @return
     */
    @PostMapping("/sendVerifyCode")
    public ApiResult sendVerifyCode(@RequestBody SendVerifyCodeDto sendVerifyCodeDto){
        sendsmsService.sendSms(sendVerifyCodeDto);
        return ApiResult.success();
    }

    /**
     * 获取图片验证码
     * @return
     */
    @GetMapping("/getImageCaptcha")
    public ApiResult getImageCaptcha(){
        ImageCaptchaRespDto imageCaptcha = imageCaptchaService.getImageCaptcha();
        return ApiResult.success(imageCaptcha);
    }
}
