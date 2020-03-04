package cn.welfarezhu.billservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UnitDTO {

    private int unitId;
    private String unitName;
    private String address;
    private String phone;
    private String description;
    private int principalId;
    private String principalName;
    private String registerDate;
}
