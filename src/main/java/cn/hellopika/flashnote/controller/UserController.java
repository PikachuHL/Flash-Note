package cn.hellopika.flashnote.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hellopika.flashnote.model.dto.request.*;
import cn.hellopika.flashnote.model.dto.response.UserInfoRespDto;
import cn.hellopika.flashnote.model.entity.User;
import cn.hellopika.flashnote.service.UserService;
import cn.hellopika.flashnote.service.WeChatService;
import cn.hellopika.flashnote.util.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private WeChatService weChatService;


    /**
     * 用户注册
     * @param dto
     * @return
     */
    @PostMapping("/userRegister")
    public ApiResult userRegister(@RequestBody UserRegisterDto dto){
        userService.userRegister(dto);
        return ApiResult.success();
    }

    /**
     * 用户登录
     * @param dto
     * @return
     */
    @PostMapping("/login")
    public ApiResult userLogin(@RequestBody UserLoginDto dto){
        User user = userService.login(dto);

        // 登录成功，返回给用户 Token
        StpUtil.setLoginId(user.getId(), dto.getDevice());
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



    /**
     * 忘记密码1：验证用户是否存在
     * @param dto
     * @return
     */
    @PostMapping("/forget/checkUser")
    public ApiResult checkUser(@RequestBody ForgetPwdCheckUserDto dto){

        String token = userService.checkUser(dto);
        return ApiResult.success(token);
    }


    /**
     * 忘记密码2：验证短信验证码
     * @param dto
     * @return
     */
    @PostMapping("/forget/checkVerifyCode")
    public ApiResult checkVerifyCode(@RequestBody ForgetPwdCheckVerifyCodeDto dto){
        String token = userService.checkVerifyCode(dto);
        return ApiResult.success(token);
    }

    /**
     * 忘记密码3：重设密码
     * @param dto
     * @return
     */
    @PostMapping("/forget/resetPwd")
    public ApiResult resetPwd(@RequestBody ForgetPwdResetPwdDto dto){
        userService.resetPwd(dto);
        return ApiResult.success();
    }

    /**
     * 用户设置
     * @return
     */
    @PostMapping("/user/setting")
    public ApiResult userSetting(@RequestBody UserSettingDto dto){
        // 从登录信息中获取 用户Id
        String userId = StpUtil.getLoginIdAsString();
        dto.setUserId(userId);

        userService.userSetting(dto);
        return ApiResult.success();
    }

    /**
     * 用户详细信息
     * @return
     */
    @PostMapping("/user/info")
    public ApiResult userInfo(){
        // 从登录信息中获取 用户Id
        String userId = StpUtil.getLoginIdAsString();

        UserInfoRespDto dto = userService.userInfo(userId);
        return ApiResult.success(dto);
    }

    /**
     * 获取绑定微信的二维码
     * @return
     */
    @GetMapping("/user/sceneQrCode")
    public ApiResult getSceneQrCode(){
        String userId = StpUtil.getLoginIdAsString();
        String qrCodeUrl = weChatService.sceneQrCode(userId);
        return ApiResult.success(qrCodeUrl);
    }

}
