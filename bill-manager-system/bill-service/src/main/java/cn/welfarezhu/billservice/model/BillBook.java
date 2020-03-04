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
@Table(name = "bill_book")
public class BillBook {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer billBookId;
    @NotNull
    private Integer unitId;
    @NotNull
    private Integer billKindId;
    private Integer operatorId;
    @NotNull
    @Pattern(regexp = "^[(a-zA-Z0-9)|.]{8,20}$")
    private String beginCode;
    @NotNull
    @Pattern(regexp = "^[(a-zA-Z0-9)|.]{8,20}$")
    private String endCode;
    private Integer billCopies;
    private Integer statusCode;
    private Integer totalMoney;
    private Integer verifyMoney;
    private Date createDate;
    private Date modifyDate;
    @Size(max = 140)
    private String description;
}
