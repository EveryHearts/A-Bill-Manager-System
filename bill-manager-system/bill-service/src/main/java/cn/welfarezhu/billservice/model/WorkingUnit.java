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
@Table(name = "working_unit")
public class WorkingUnit {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer unitId;
    @NotNull
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9]{1,30}$")
    private String unitName;
    private String unitAddress;
    private String unitPhone;
    @Size(max = 140)
    private String unitDescription;
    @NotNull
    private Integer unitPrincipalId;
    private Date registerDate;
    private Date modifyDate;
}
