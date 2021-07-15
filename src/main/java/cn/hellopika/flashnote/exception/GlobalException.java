package cn.hellopika.flashnote.exception;

import cn.hellopika.flashnote.util.result.ApiResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @date: 2021/6/16
 * @author: pikachu
 * @description: 全局异常处理
 **/

@RestControllerAdvice
public class GlobalException {

    @CrossOrigin
    @ExceptionHandler(ServiceException.class)
    public ApiResult serviceExceptionHandler(ServiceException serviceException){
        return ApiResult.fail(serviceException.getMessage());
    }
}
