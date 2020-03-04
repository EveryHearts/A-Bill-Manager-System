package cn.welfarezhu.billservice.enums;

public enum RoleCode {

    MANAGER(0,    //管理员
            "管理员",
            false,
            false,
            true,
            false,
            false,
            true,
            false,
            false),
    REGISTER(3,    //注册人（票据发放）
            "负责人",
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true),
    MONITOR(2,    //监督员
            "票据经办人",
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true),
    OPERATOR(1,    //操作员（票据核销）
            "票据领收员",
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true);

    private int roleNum;
    private String roleValue;
    private boolean addRole;
    private boolean modifyRoleInfo;
    private boolean modifyRoleStatus;
    private boolean modifyUnitInfo;
    private boolean addBillKind;
    private boolean modifyBillKind;
    private boolean addBill;
    private boolean modifyBillStatus;

    RoleCode(int roleNum, String roleValue, boolean addRole, boolean modifyRoleInfo, boolean modifyRoleStatus, boolean modifyUnitInfo, boolean addBillKind, boolean modifyBillKind, boolean addBill, boolean modifyBillStatus) {
        this.roleNum = roleNum;
        this.roleValue = roleValue;
        this.addRole = addRole;
        this.modifyRoleInfo = modifyRoleInfo;
        this.modifyRoleStatus = modifyRoleStatus;
        this.modifyUnitInfo = modifyUnitInfo;
        this.addBillKind = addBillKind;
        this.modifyBillKind = modifyBillKind;
        this.addBill = addBill;
        this.modifyBillStatus = modifyBillStatus;
    }

    public int roleNum(){
        return this.roleNum;
    }
    public String roleValue(){
        return this.roleValue;
    }
    public boolean addRole(){
        return this.addRole;
    }
    public boolean mmodifyRoleInfo(){
        return this.modifyRoleInfo;
    }
    public boolean modifyRoleStatus(){
        return this.modifyRoleStatus;
    }
    public boolean modifyUnitInfo(){
        return this.modifyUnitInfo;
    }
    public boolean addBillKind(){
        return this.addBillKind;
    }
    public boolean modifyBillKind(){
        return this.modifyBillKind;
    }
    public boolean addBill(){
        return this.addBill;
    }
    public boolean modifyBillStatus(){
        return this.modifyBillStatus;
    }
}
