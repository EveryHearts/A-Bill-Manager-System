package cn.welfarezhu.billweb.response;

import cn.welfarezhu.billweb.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResult {
    private Integer status;
    private String message;
    private String exception;

    public static ErrorResult error(ResultCode resultCode,Throwable e,String message){
        ErrorResult result=ErrorResult.error(resultCode,e);
        result.setMessage(message);
        return result;
    }

    public static ErrorResult error(ResultCode resultCode,Throwable e){
        ErrorResult result=new ErrorResult();
        result.setStatus(resultCode.status());
        result.setMessage(resultCode.message());
        result.setException(e.getClass().getName());
        return result;
    }
}
