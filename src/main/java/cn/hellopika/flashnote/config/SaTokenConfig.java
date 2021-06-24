package cn.hellopika.flashnote.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @date: 2021/6/16
 * @author: pikachu
 * @description: TODO 类描述
 **/

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * SaToken的登录拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaRouteInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(Arrays.asList("/userRegister", "/login", "/common/**", "/forget/**"));
    }
}
