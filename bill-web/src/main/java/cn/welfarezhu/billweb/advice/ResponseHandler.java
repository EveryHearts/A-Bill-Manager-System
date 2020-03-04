package cn.welfarezhu.billweb.advice;

import cn.welfarezhu.billweb.response.ErrorResult;
import cn.welfarezhu.billweb.response.Result;
import io.github.yidasanqian.utils.JsonUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackages = "cn.welfarezhu.billweb",annotations = {ResponseBody.class,RestController.class})
public class ResponseHandler implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof ErrorResult){
            ErrorResult result=(ErrorResult)o;
            return Result.faill(result.getStatus(),result.getMessage());
        }else if (o instanceof String){
            return JsonUtil.toJsonString(Result.succ(o));
        }
        return Result.succ(o);
    }
}
