package cn.welfarezhu.billservice.enums;

public enum MessageStatusCode {
    READED(1,"已读"),
    WAIT_READ(0,"未读");

    private int statusCode;
    private String status;

    MessageStatusCode(int statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }

    public int statusCode() {
        return statusCode;
    }

    public String status() {
        return status;
    }
}
