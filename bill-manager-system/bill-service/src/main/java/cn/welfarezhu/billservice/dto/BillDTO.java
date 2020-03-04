package cn.welfarezhu.billservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BillDTO {

    private int billId;
    private int billBookId;
    private String billKind;
    private int operatorId;
    private String operator;
    private String code;
    private int money;
    private int statusCode;
    private String status;
    private String description;
    private String createDate;
    private String modifyDate;
}
