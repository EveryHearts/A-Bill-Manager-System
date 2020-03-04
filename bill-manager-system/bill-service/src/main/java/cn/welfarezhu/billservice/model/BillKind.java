package cn.welfarezhu.billservice.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Table(name = "bill_kind")
public class BillKind {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer billKindId;
    private Integer unitId;
    @Size(max = 30)
    @NotNull
    private String kindName;
    private Integer statusCode;
    private Date createDate;
    private Date modifyDate;
}
