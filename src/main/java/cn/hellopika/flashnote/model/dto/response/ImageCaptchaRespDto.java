package cn.hellopika.flashnote.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: 获取图片验证码 返回的数据封装类
 **/
@Data
@AllArgsConstructor
public class ImageCaptchaRespDto {

    private String captchaToken;  // 生成的图片验证码对应的唯一标志
    private String imageBase64;  // 图片的 base64 编码
}
