package cn.hellopika.flashnote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2021/7/3
 * @author: pikachu
 * @description: 微信开发相关参数的配置类
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WeChatConfig {
    private String id;
    private String token;
    private String appId;
    private String appSecret;
}
