package cn.welfarezhu.billservice.enums;

public enum BillStatusCode {

    PROVIDE(0,"已发放"),    //已发放
    RECEIVE(1,"已领收"),    //已领收
    USED(2,"已使用"),       //已使用
    WAIT_VERIFY(3,"核销待确认"),    //核销待确认
    CANCEL(4,"已核销"),     //核销
    BANNING(5,"作废待确认"),    //作废待确认
    BANNED(6,"已作废");     //已作废

    private int statusCode;
    private String status;

    BillStatusCode(int statusCode,String status) {
        this.statusCode = statusCode;
        this.status=status;
    }

    public int statusCode(){
        return this.statusCode;
    }
    public String status(){
        return status;
    }
}
