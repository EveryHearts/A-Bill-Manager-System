package cn.welfarezhu.billweb.enums;

public enum ResultCode {
    SUCCESS(0,"成功"),
    FAIL(1,"失败"),
    SYSTEM_ERROR(1000,"系统异常"),
    PARAM_IS_INVAILD(1001,"参数错误"),
    USER_IS_INVAILD(1002,"用户非法"),
    USER_IS_EXISTED(1003,"用户已存在");
    private Integer status;
    private String message;
    ResultCode(Integer status,String message){
        this.status=status;
        this.message=message;
    }
    public Integer status(){
        return this.status;
    }
    public String message(){
        return this.message;
    }
}
