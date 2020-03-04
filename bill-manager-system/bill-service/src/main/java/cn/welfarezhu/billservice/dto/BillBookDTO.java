package cn.welfarezhu.billservice.dto;

import lombok.Data;

@Data
public class BillBookDTO {

    private int billBookId;
    private int unitId;
    private String unit;
    private String kindName;
    private int operatorId;
    private String operator;
    private String beginCode;
    private String endCode;
    private int copies;
    private String status;
    private int statusCode;
    private int totalMoney;
    private int verifyMoney;
    private String description;
    private String createDate;
    private String modifyDate;
}
