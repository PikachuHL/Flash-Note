package cn.hellopika.flashnote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2021/7/3
 * @author: pikachu
 * @description: TODO 类描述
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
