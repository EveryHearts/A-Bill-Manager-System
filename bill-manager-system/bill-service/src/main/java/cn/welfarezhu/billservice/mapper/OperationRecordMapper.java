package cn.welfarezhu.billservice.mapper;

import cn.welfarezhu.billservice.model.OperationRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface OperationRecordMapper extends Mapper<OperationRecord> {

    //根据unitId查询Record
    @Select(value = "select * from operation_record where unit_id = #{unitId} order by operation_date desc")
    List<OperationRecord> queryRecordByUnitId(int unitId);

    //根据userId查询Record
    @Select(value = "select * from operation_record where user_id = #{userId} order by operation_date desc")
    List<OperationRecord> queryRecordByUserId(int userId);

    @Select(value = "select * from operation_record where operation_sort = #{operationSort} order by operation_date desc")
    List<OperationRecord> queryRecordByOperationSort(int operationSort);

    //根据unitId和日期间隔查询Record
    @Select(value = "select * from operation_record where " +
            "unit_id = #{unitId} " +
            "and operation_date between #{beginTime} and #{endTime} " +
            "order by operation_date desc")
    List<OperationRecord> queryRecordByDateAndUnitId(@Param("unitId") int unitId,@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    //根据userId和日期间隔查询Record
    @Select(value = "select * from operation_record where " +
            "user_id = #{userId} " +
            "and operation_date between #{beginTime} and #{endTime} " +
            "order by operation_date desc")
    List<OperationRecord> queryRecordByDateAndUserId(@Param("userId") int userId,@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);
}
