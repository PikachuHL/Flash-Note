package cn.hellopika.flashnote.service;

import cn.hellopika.flashnote.model.dto.response.ImageCaptchaRespDto;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: 响应图片验证码的service层接口
 **/
public interface ImageCaptchaService {

    ImageCaptchaRespDto getImageCaptcha();
}
