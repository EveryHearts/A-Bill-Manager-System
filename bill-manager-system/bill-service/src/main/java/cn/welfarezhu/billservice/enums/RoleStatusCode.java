package cn.welfarezhu.billservice.enums;

public enum RoleStatusCode {

    NORMAL(0,"正常"),    //正常
    BAN(1,"封禁"),       //封禁
    LOGOUT(2,"注销"),   //注销
    CHECKING(3,"待审核");

    private int statusCode;
    private String status;

    RoleStatusCode(int statusCode,String status){
        this.statusCode=statusCode;
        this.status=status;
    }
    public int statusCode(){
        return this.statusCode;
    }
    public String status(){
        return this.status;
    }
}
