package cn.hellopika.flashnote.exception;

/**
 * @date: 2021/6/13
 * @author: pikachu
 * @description: 业务层 专用异常类
 **/

public class ServiceException extends RuntimeException {

    public ServiceException(){}

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message, Throwable cause){
        super(message, cause);
    }

}
