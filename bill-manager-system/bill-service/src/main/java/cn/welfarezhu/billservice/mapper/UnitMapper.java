package cn.welfarezhu.billservice.mapper;

import cn.welfarezhu.billservice.model.WorkingUnit;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface UnitMapper extends Mapper<WorkingUnit> {

    //按照单位ID查询单位名称
    @Select(value = "select unit_name from working_unit where unit_id = #{unitId}")
    String queryUnitName(int unitId);

    //根据ID修改principalId
    @Update(value = "update working_unit set modify_date = now() , unit_principal_id = #{principalId} where unit_id = #{unitId}")
    int updatePrincipalId(@Param("unitId") int unitId,@Param("principalId") int principalId);

    //根据ID查询principalId
    @Select(value = "select unit_principal_id from working_unit where unit_id = #{unitId}")
    int queryPrincipalId(int unitId);

    //根据principalId查询unitName
    @Select(value = "select unit_name from working_unit where unit_principal_id = #{principalId}")
    String queryUnitNameByPrincipalId(int principalId);

    @Select(value = "select count(*) from working_unit where unit_name = #{name}")
    int countUnitName(String name);
}
