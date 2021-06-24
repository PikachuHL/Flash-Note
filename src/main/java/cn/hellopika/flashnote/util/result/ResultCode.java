package cn.hellopika.flashnote.util.result;

/**
 * @date: 2021/6/15
 * @author: pikachu
 * @description: TODO 类描述
 **/
public interface ResultCode {

    boolean isSuccess();

    Integer getCode();

    String getMessage();

}
