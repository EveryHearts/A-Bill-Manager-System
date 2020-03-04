package cn.welfarezhu.billservice.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Table(name = "message_info")
public class MessageInfo {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer messageId;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer receptorId;
    @Size(max = 140)
    private String content;
    private Integer messageSort;
    private Integer targetId;
    private Integer statusCode;
    private Date createDate;
    private Date modifyDate;
}
