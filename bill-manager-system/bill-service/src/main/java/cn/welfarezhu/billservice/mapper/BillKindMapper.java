package cn.welfarezhu.billservice.mapper;

import cn.welfarezhu.billservice.model.BillKind;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BillKindMapper extends Mapper<BillKind> {

    //根据ID查询kind name
    @Select(value = "select kind_name from bill_kind where bill_kind_id = #{kindId}")
    String queryKindName(int kindId);

    //根据ID修改status Code
    @Update(value = "update bill_kind set modify_date = now() , status_code = #{statusCode} where bill_kind_id = #{kindId}")
    int updateStatusCode(@Param("kindId") int kindId,@Param("statusCode") int statusCode);

    //根据unitId查询billKind
    @Select(value = "select * from bill_kind where unit_id = #{unitId} order by create_date desc")
    List<BillKind> queryBillKindByUnitId(int unitId);

    @Select(value = "select count(*) from bill_kind where unit_id = #{unitId} and kind_name = #{name}")
    int countKindName(@Param("unitId") int unitId,@Param("name") String name);
}