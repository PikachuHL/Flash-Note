package cn.hellopika.flashnote.util;

/**
 * @date: 2021/6/27
 * @author: pikachu
 * @description: 系统中的所有常量
 **/
public interface SysConst {

    String USER_PASSWORD_SALT = "lucky*dog@$go";

    interface RedisPrefix{

        String SEND_VERIFY_CODE = "sms:verify:send:";
        String VERIFY_CODE = "sms:verify:";

        String LOGIN_LOCK = "login:lock:";
        String LOGIN_PASSWORD_ERROR = "login:pwd:error:";

        String IMAGE_CAPTCHA_TEXT = "imageCaptcha:text:";

        String FORGET_PASSWORD_SEND_VERIFY_CODE = "forgetpwd:sendverifycode:";
        String FORGET_PASSWORD_RESET = "forgetPwd:resetPwd:";

    }
}
