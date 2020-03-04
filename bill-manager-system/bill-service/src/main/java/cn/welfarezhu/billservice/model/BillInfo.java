package cn.welfarezhu.billservice.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Table(name = "bill_info")
public class BillInfo {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer billId;
    @NotNull
    private Integer billBookId;
    @NotNull
    private Integer billKindId;
    private Integer operatorId;
    @NotNull
    @Pattern(regexp = "^[(a-zA-Z0-9)|.]{8,20}$")
    private String billCode;
    private Integer billMoney;
    private Integer statusCode;
    private Date createDate;
    private Date modifyDate;
    @Size(max = 140)
    private String description;
}
