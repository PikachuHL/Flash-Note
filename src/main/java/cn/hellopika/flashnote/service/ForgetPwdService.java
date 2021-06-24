package cn.hellopika.flashnote.service;

import cn.hellopika.flashnote.model.dto.ForgetPwdCheckUserDto;

/**
 * @date: 2021/6/17
 * @author: pikachu
 * @description: TODO 类描述
 **/
public interface ForgetPwdService {
    /**
     * 检查用户是否存在
     * @param forgetPwdCheckUserDto
     */
    void checkUser(ForgetPwdCheckUserDto forgetPwdCheckUserDto);
}
