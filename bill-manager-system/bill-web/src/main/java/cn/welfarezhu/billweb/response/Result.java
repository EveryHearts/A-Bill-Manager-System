package cn.welfarezhu.billweb.response;

import cn.welfarezhu.billweb.enums.ResultCode;
import lombok.Data;

@Data
public class Result<T> {

    private Integer status;
    private String message;
    private T data;

    public static Result succ(){
        Result result=new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static Result succ(Object data){
        Result result=new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result faill(Integer status,String message){
        Result result=new Result();
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }

    public static Result faill(ResultCode resultCode){
        Result result=new Result();
        result.setResultCode(resultCode);
        return result;
    }

    private void setResultCode(ResultCode resultCode){
        this.status=resultCode.status();
        this.message=resultCode.message();
    }
}
