package cn.hellopika.flashnote.util.result;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @date: 2021/6/15
 * @author: pikachu
 * @description: TODO 类描述
 **/

@ToString
@AllArgsConstructor
public enum CommonResultCode implements ResultCode {
    SUCCESS(true, 0, "success"),
    FAIL(false, -1, "error");

    private boolean success;
    private Integer code;
    private String message;

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
