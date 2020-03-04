package cn.welfarezhu.billservice.mapper;

import cn.welfarezhu.billservice.model.BillInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BillMapper extends Mapper<BillInfo> {

    //根据ID查询operatorId
    @Select(value = "select operator_id from bill_info where bill_id = #{billId}")
    int queryOperatorId(int billId);

    //根据ID查询statusCode
    @Select(value = "select status_code from bill_info where bill_id = #{billId}")
    int queryStatusCode(int billId);

    //根据ID修改statusCode
    @Update(value = "update bill_info set modify_date = now() ,status_code = #{statusCode} where bill_id = #{billId}")
    int updateStatusCode(@Param("billId") int billId,@Param("statusCode") int statusCode);

    //根据ID修改description
    @Update(value = "update bill_info set modify_date = now() , description = #{desc} where bill_id = #{billId}")
    int updateDescription(@Param("billId") int billId,@Param("desc") String desc);

    //根据bookId和statusCode合计billMoney
    @Select(value = "select sum(bill_money) from bill_info where bill_book_id = #{bookId} and status_code = #{statusCode}")
    int countMoneyByBookIdAnfStatusCode(@Param("bookId") int bookId,@Param("statusCode") int statusCode);

    //根据billId List批量修改statusCode
    @Update(value = "<script>" +
            "update bill_info set modify_date = now() , status_code = #{statusCode} where bill_id in" +
            "<foreach item='billId' index='index' collection='billIdList' open='(' separator=',' close=')'>" +
            "#{billId}" +
            "</foreach>" +
            "</script>")
    int updateStatusByBillIdList(@Param("billIdList") List<Integer> idList,@Param("statusCode") int statusCode);

    //根据bookId和statusCode查询billId
    @Select(value = "select bill_id from bill_info where bill_book_id = #{bookId} and status_code = #{statusCode} order by bill_code asc")
    List<Integer> queryBillIdByBookIdAndStatusCode(@Param("bookId") int bookId,@Param("statusCode") int statusCode);

    //合计Bill
    @Select(value = "select count(*) from bill_info where bill_code = #{billCode}")
    int countBillByBillCode(@Param("billCode") String billCode);

    //根据bookId查询Bill
    @Select(value = "select * from bill_info where bill_book_id = #{bookId} order by bill_code asc")
    List<BillInfo> queryBillByBookId(int bookId);

    //根据bookId和statusCode查询Bill
    @Select(value = "select * from bill_info where bill_book_id = #{bookId} and status_code = #{statusCode} order by bill_code asc")
    List<BillInfo> queryBillByBookIdAndStatusCode(@Param("bookId") int bookId,@Param("statusCode") int statusCode);

    @Select(value = "select bill_book_id from bill_info where bill_id = #{billId}")
    int queryBookIdByBillId(int billId);

    @Select(value = "select bill_money from bill_info where bill_id = #{billId}")
    int queryBillMoney(int billId);

    @Update(value = "update bill_info set operator_id = #{userId} where bill_id = #{billId}")
    int setOperatorId(@Param("billId") int bookId,@Param("userId") int userId);

    @Select(value = "select bill_book_id from bill_info where bill_code = #{code}")
    int queryBookIdByBillCode(String code);
}
