package cn.welfarezhu.billservice.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Table(name = "user_info")
public class UserInfo {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer userId;
    private Integer unitId;
    @NotNull
    private Integer roleCode;
    @NotNull
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9]{1,10}$")
    private String userName;
    @NotNull
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")
    private String phone;
    @Size(max = 140)
    private String address;
    @Range(min = 1,max = 100)
    private Integer age;
    @Range(min = 0,max = 1)
    private Integer gender;
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9]{8,18}$")
    @NotNull
    private String account;
    @NotNull
    @Size(max = 100)
    private String password;
    private Integer statusCode;
    private Date createDate;
    private Date modifyDate;
    @Size(max = 140)
    private String description;
}
