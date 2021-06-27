package cn.hellopika.flashnote.service;

import cn.hellopika.flashnote.model.dto.*;
import cn.hellopika.flashnote.model.entity.User;

import java.util.List;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 用户相关 业务层 接口
 **/
public interface UserService {

    /**
     * 用户注册
     * @param dto
     */
    void userRegister(UserRegisterDto dto);

    /**
     * 用户登录
     * @param dto
     * @return
     */
    User login(UserLoginDto dto);

    /**
     * 忘记密码1：检查用户是否存在
     * @param dto
     */
    String checkUser(ForgetPwdCheckUserDto dto);

    /**
     * 忘记密码2：验证短信验证码
     * @param dto
     * @return
     */
    String checkVerifyCode(ForgetPwdCheckVerifyCodeDto dto);

    /**
     * 忘记密码3：重设密码
     * @param dto
     */
    void resetPwd(ForgetPwdResetPwdDto dto);
}
