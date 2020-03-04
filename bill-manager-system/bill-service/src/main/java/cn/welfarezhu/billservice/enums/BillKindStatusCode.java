package cn.welfarezhu.billservice.enums;

public enum BillKindStatusCode {
    NORMAL(0,"正常"),
    BANNED(1,"已废止");

    private int statusCode;
    private String status;

    BillKindStatusCode(int statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }

    public int statusCode(){
        return this.statusCode;
    }
    public String status(){
        return this.status;
    }
}
