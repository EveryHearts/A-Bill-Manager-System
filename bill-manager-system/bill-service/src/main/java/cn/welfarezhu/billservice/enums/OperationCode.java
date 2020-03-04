package cn.welfarezhu.billservice.enums;

public enum OperationCode {

    ADD_NEW_ROLE(1,"添加新角色"),
    MODIFY_USER_INFO(2,"修改角色信息"),
    MODIFY_USER_PASS(3,"修改角色账号密码"),
    BANNED_ROLE(4,"封禁用户"),
    LOGOUT_ROLE(5,"注销用户"),
    ADD_NEW_BILL_BOOK(6,"添加票据薄"),
    RECEIVE(7,"领用票据"),
    USED(8,"标记使用票据"),
    CANCELED(9,"核销票据"),
    BANNED_BILL(10,"作废票据"),
    ADD_NEW_KIND(11,"添加新的票据种类");


    private int operationNum;
    private String operation;

    OperationCode(int operationNum, String operation) {
        this.operationNum = operationNum;
        this.operation = operation;
    }
    public int operationNum(){
        return this.operationNum;
    }
    public String operation(){
        return this.operation;
    }
}
