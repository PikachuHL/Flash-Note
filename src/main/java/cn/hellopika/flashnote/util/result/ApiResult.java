package cn.hellopika.flashnote.util.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/**
 * @date: 2021/6/15
 * @author: pikachu
 * @description: controller 中的方法返回值
 **/

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)  // 在转json的时候把null排除
public class ApiResult<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;

    public ApiResult(){}

    public ApiResult(ResultCode resultCode){
        this(resultCode, null);
    }

    public ApiResult(T data){
        this(CommonResultCode.SUCCESS, data);
    }

    public ApiResult(ResultCode resultCode, T data){
        this.success = resultCode.isSuccess();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    /**
     * 操作成功
     * @return
     */
    public static ApiResult success(){
        return new ApiResult(CommonResultCode.SUCCESS);
    }

    public static <T> ApiResult success(T data){
        return new ApiResult(data);
    }

    /**
     * 操作失败
     * @return
     */
    public static ApiResult fail(){
        return new ApiResult(CommonResultCode.FAIL);
    }

    public static ApiResult fail(String message) {
        ApiResult result = new ApiResult<>();
        result.success = false;
        result.code = -1;
        result.message = message;
        return result;
    }


}
