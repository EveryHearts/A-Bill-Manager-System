package cn.welfarezhu.billservice.dto;

import cn.welfarezhu.billservice.enums.RoleCode;
import lombok.Data;
import java.util.Date;

@Data
public class UserDTO {

    private int userId;
    private int unitId;
    private String unit;
    private String role;
    private String name;
    private String phone;
    private String address;
    private int age;
    private String gender;
    private int statusCode;
    private String status;
    private String description;
    private String createDate;
    private int roleNum;
    private String roleValue;
    private RoleCode roleCode;
}
