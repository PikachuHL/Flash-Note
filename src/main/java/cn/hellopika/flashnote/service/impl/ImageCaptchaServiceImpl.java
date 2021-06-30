package cn.hellopika.flashnote.service.impl;

import cn.hellopika.flashnote.exception.ServiceException;
import cn.hellopika.flashnote.model.dto.response.ImageCaptchaRespDto;
import cn.hellopika.flashnote.service.ImageCaptchaService;
import cn.hellopika.flashnote.util.ImageCaptcha;
import cn.hellopika.flashnote.util.SysConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: TODO 类描述
 **/

@Service
@Slf4j
public class ImageCaptchaServiceImpl implements ImageCaptchaService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public ImageCaptchaRespDto getImageCaptcha() {
        // 使用工具类获取验证码图片及文本
        ImageCaptcha imageCaptcha = new ImageCaptcha();
        BufferedImage image = imageCaptcha.getImage();
        String text = imageCaptcha.getText();

        // 获取图片验证码的base64编码
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            ImageCaptcha.output(image, outputStream);
            byte[] bytes = outputStream.toByteArray();
            outputStream.flush();

            String imageBase64 = "data:image/jpg;base64, " + Base64.encodeBase64String(bytes);

            String captchaToken = UUID.randomUUID().toString();  // 获取一个 UUID 作为验证码的 token
            RBucket<String> imageCaptchaTextCache = redissonClient.getBucket(SysConst.RedisPrefix.IMAGE_CAPTCHA_TEXT + captchaToken);
            imageCaptchaTextCache.set(text, 10, TimeUnit.MINUTES);  // 把图片验证码的文本放到redis中

            log.info("图片验证码生成成功：{}", text);

            return new ImageCaptchaRespDto(captchaToken, imageBase64);
        } catch (IOException e) {
            throw new ServiceException("图片验证码生成失败");
        }
    }
}
