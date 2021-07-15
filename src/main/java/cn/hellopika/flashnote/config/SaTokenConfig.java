package cn.hellopika.flashnote.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouterUtil;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @date: 2021/6/16
 * @author: pikachu
 * @description: web相关的一些配置
 **/

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 解决跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*");
    }

    /**
     * 根据url控制访问权限
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //http://sa-token.dev33.cn/doc/index.html#/use/route-check
        registry.addInterceptor(new SaRouteInterceptor(((request, response, handler) -> {
            if(!request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
                SaRouterUtil.match(Arrays.asList("/**"), Arrays.asList(
                        "/common/**",
                        "/userRegister",
                        "/login",
                        "/forget/**",
                        "/wechat/**"
                ), () -> StpUtil.checkLogin());
            }
        }))).addPathPatterns("/**");
    }


//    /**
//     * SaToken的登录拦截器
//     *
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SaRouteInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns(Arrays.asList(
//                        "/userRegister",
//                        "/login",
//                        "/common/**",
//                        "/forget/**",
//                        "/wechat/**"));
//    }

//    /**
//     * 跨域支持
//     *
//     * @param registry
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**");
//    }

}
