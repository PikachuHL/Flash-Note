package cn.hellopika.flashnote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 发送短信 配置类
 **/
@Configuration
@ConfigurationProperties(prefix = "sms")
@Data
public class SmsConfig {

    private String username;
    private String password;
}
