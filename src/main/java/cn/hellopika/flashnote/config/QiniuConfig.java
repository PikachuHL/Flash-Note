package cn.hellopika.flashnote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2021/6/30
 * @author: pikachu
 * @description: 上传七牛云相关参数的配置类
 **/
@Configuration
@ConfigurationProperties("qiniu")
@Data
public class QiniuConfig {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;
}
