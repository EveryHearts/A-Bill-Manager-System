package cn.welfarezhu.billweb.exception;

import cn.welfarezhu.billweb.enums.ResultCode;
import cn.welfarezhu.billweb.response.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResult handleThrowable(Throwable e, HttpServletRequest request){
        ErrorResult result=ErrorResult.error(ResultCode.SYSTEM_ERROR,e);
        log.error("time : {} | url : {} | Exception : {} | message : {}",new Date(),request.getRequestURL(),e.getClass().getName(),e.getMessage());
        return result;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResult handleBindException(BindException e,HttpServletRequest request){
        String msg=this.handle(e.getBindingResult().getFieldErrors());
        ErrorResult result=ErrorResult.error(ResultCode.PARAM_IS_INVAILD,e,msg);
        log.error("time : {} | url : {} | Exception : {} | message : {}",new Date(),request.getRequestURL(),e.getClass().getName(),msg);
        return result;
    }

    private String handle(List<FieldError> fieldErrors){
        StringBuffer stringBuffer=new StringBuffer();
        for (FieldError fieldError:fieldErrors){
            stringBuffer.append(fieldError.getField());
            stringBuffer.append("=[");
            stringBuffer.append(fieldError.getDefaultMessage());
            stringBuffer.append("] ");
        }
        return stringBuffer.toString();
    }
}
