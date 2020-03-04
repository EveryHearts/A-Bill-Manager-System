package cn.welfarezhu.billservice.dto.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

@Data
public class ResultDataMap<T> {
    private List<T> result;
    private PageInfo pageInfo;
}
