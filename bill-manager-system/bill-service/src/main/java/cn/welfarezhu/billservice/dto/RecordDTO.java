package cn.welfarezhu.billservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RecordDTO {

    private int recordId;
    private int unitId;
    private String unit;
    private int userId;
    private String user;
    private String operation;
    private String objectSort;
    private int objectId;
    private String operationDate;
}
