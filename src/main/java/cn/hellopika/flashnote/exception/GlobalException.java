package cn.hellopika.flashnote.exception;

import cn.hellopika.flashnote.util.result.ApiResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @date: 2021/6/16
 * @author: pikachu
 * @description: 全局异常处理
 **/

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ApiResult serviceExceptionHandler(ServiceException serviceException){
        return ApiResult.fail(serviceException.getMessage());
    }
}
