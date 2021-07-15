package cn.hellopika.flashnote.util.result;

/**
 * @date: 2021/6/15
 * @author: pikachu
 * @description: 定义统一的result格式
 **/
public interface ResultCode {

    boolean isSuccess();

    Integer getCode();

    String getMessage();

}
