package cn.hellopika.flashnote.service;

import cn.hellopika.flashnote.model.dto.UserLoginDto;
import cn.hellopika.flashnote.model.entity.User;
import cn.hellopika.flashnote.model.dto.UserRegisterDto;

import java.util.List;

/**
 * @date: 2021/6/6
 * @author: pikachu
 * @description: 用户相关 业务层 接口
 **/
public interface UserService {

    /**
     * 用户注册
     * @param userRegisterDto
     */
    void userRegister(UserRegisterDto userRegisterDto);

    /**
     * 用户登录
     * @param userLoginDto
     * @return
     */
    User login(UserLoginDto userLoginDto);
}
