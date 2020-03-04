package cn.welfarezhu.billservice.enums;

public enum ObjectSortCode {
    USER(0,"用户"),
    BILL(1,"票据"),
    BILL_BOOK(2,"票据薄"),
    UNIT(3,"单位"),
    BILL_KIND(4,"票据种类");

    private int sortCode;
    private String sort;

    ObjectSortCode(int sortCode, String sort) {
        this.sortCode = sortCode;
        this.sort = sort;
    }

    public int sortCode() {
        return sortCode;
    }

    public String sort() {
        return sort;
    }
}
