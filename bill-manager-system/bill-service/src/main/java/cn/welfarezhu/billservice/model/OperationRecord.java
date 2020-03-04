package cn.welfarezhu.billservice.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "operation_record")
public class OperationRecord {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer recordId;
    private Integer unitId;    //单位ID
    private Integer userId;    //操作者ID
    private Integer operationSort;    //操作类型
    private Integer objectSort;    //对象类型
    private Integer objectId;     //对象ID
    private Date operationDate;
}
