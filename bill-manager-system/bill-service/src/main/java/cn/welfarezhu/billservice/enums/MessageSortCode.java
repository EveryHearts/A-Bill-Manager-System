package cn.welfarezhu.billservice.enums;

public enum MessageSortCode {
    USER_MSG(0,"用户消息"),
    SYSTEM_MSG(1,"系统消息");

    private int sortCode;
    private String sort;

    MessageSortCode(int sortCode, String sort) {
        this.sortCode = sortCode;
        this.sort = sort;
    }

    public int sortCode(){
        return this.sortCode;
    }
    public String sort(){
        return this.sort;
    }
}
