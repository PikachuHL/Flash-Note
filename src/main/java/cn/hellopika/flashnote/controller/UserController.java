package cn.hellopika.flashnote.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hellopika.flashnote.model.dto.UserLoginDto;
import cn.hellopika.flashnote.model.entity.User;
import cn.hellopika.flashnote.model.dto.UserRegisterDto;
import cn.hellopika.flashnote.service.UserService;
import cn.hellopika.flashnote.util.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 用户相关 Controller
 **/

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterDto
     * @return
     */
    @PostMapping("/userRegister")
    public ApiResult userRegister(@RequestBody UserRegisterDto userRegisterDto){
        userService.userRegister(userRegisterDto);
        return ApiResult.success();
    }

    /**
     * 用户登录
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login")
    public ApiResult userLogin(@RequestBody UserLoginDto userLoginDto){
        User user = userService.login(userLoginDto);

        // 登录成功，返回给用户 Token
        StpUtil.setLoginId(user.getId(), userLoginDto.getDevice());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        return ApiResult.success(tokenInfo);
    }

    /**
     * 安全退出
     * @return
     */
    @PostMapping("/logout")
    public ApiResult userLogout(){
        log.info("用户 {} 从 {} 安全退出", StpUtil.getLoginId(), StpUtil.getLoginDevice());
        // 安全退出，Token立即失效
        StpUtil.logout();
        return ApiResult.success();
    }



}
