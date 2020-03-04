package cn.welfarezhu.billservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BillKindDTO {

    private int billKindId;
    private int unitId;
    private String unit;
    private String kindName;
    private int statusCode;
    private String createDate;
}
