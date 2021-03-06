package cn.hellopika.flashnote.service;

import cn.hellopika.flashnote.model.dto.request.*;
import cn.hellopika.flashnote.model.dto.response.UserInfoRespDto;
import cn.hellopika.flashnote.model.entity.User;

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

    /**
     * 用户设置
     * @param dto
     */
    void userSetting(UserSettingDto dto);

    /**
     * 用户详细信息
     * @param userid
     * @return
     */
    UserInfoRespDto userInfo(String userid);
}
