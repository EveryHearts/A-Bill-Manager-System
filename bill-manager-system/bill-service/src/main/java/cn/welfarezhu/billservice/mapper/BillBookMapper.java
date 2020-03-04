package cn.welfarezhu.billservice.mapper;

import cn.welfarezhu.billservice.model.BillBook;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BillBookMapper extends Mapper<BillBook> {

    //根据ID查询unitId
    @Select(value = "select unit_id from bill_book where bill_book_id = #{bookId}")
    int queryUnitId(int bookId);

    //根据ID查询operatorId
    @Select(value = "select operator_id from bill_book where bill_book_id = #{bookId}")
    int queryOperatorId(int bookId);

    //根据ID查询statusCode
    @Select(value = "select status_code from bill_book where bill_book_id = #{bookId}")
    int queryStatusCode(int bookId);

    //根据ID修改statusCode
    @Update(value = "update bill_book set modify_date = now() , status_code = #{statusCode} where bill_book_id = #{bookId}")
    int updateStatusCode(@Param("bookId") int bookId,@Param("statusCode") int statusCode);

    //根据ID修改verifyMoney
    @Update(value = "update bill_book set modify_date = now() , verify_money = #{verifyMoney} where bill_book_id = #{bookId}")
    int updateVerifyMoney(@Param("bookId") int bookId,@Param("verifyMoney") int verifyMoney);

    //根据ID修改description
    @Update(value = "update bill_book set modify_date = now() , description = #{desc} where bill_book_id = #{bookId}")
    int updateDescription(@Param("bookId") int bookId,@Param("desc") String desc);

    //根据ID查询billId
    @Select(value = "select bill_id from bill_book where bill_book_id = #{bookId}")
    List<Integer> queryBillId(int bookId);

    //根据unitId查询billBook
    @Select(value = "select * from bill_book where unit_id = #{unitId} order by create_date desc")
    List<BillBook> queryBillBookByUnitId(int unitId);

    //根据unitId和statusCode查询BillBook
    @Select(value = "select * from bill_book where unit_id = #{unitId} and status_code = #{statusCode} order by create_date desc")
    List<BillBook> queryBillBookByUnitAndStatus(@Param("unitId") int unitId,@Param("statusCode") int statusCode);

    //根据unitId和日期间隔查询核销金额总计
    @Select(value = "select sum(verify_money) from bill_book where status_code = 4 and unit_id = #{unitId} and create_date between #{beginTime} and #{endTime}")
    int countVerifyMoneyByDate(@Param("unitId") int unitId,@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    @Select(value = "select * from bill_book where status_code = 4 and unit_id = #{unitId} and create_date between #{beginTime} and #{endTime}")
    List<BillBook> queryVerifyBookByDate(@Param("unitId") int unitId,@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    //根据bookId List查询核销金额总计
    @Select(value = "<script>" +
            "select sum(verify_money) from bill_book where bill_book_id in " +
            "<foreach item='bookId' index='index' collection='idList' open='(' separator=',' close=')'>" +
            "#{bookId}" +
            "</foreach>" +
            "</script>")
    int countVerifyMoneyByBookIdList(@Param("idList") List<Integer> idList);

    @Select(value = "select verify_money from bill_book where bill_book_id =#{bookId}")
    int queryVerifyMoney(int bookId);

    @Update(value = "update bill_book set operator_id = #{userId} where bill_book_id = #{bookId}")
    int setOperatorId(@Param("bookId") int bookId,@Param("userId") int userId);
}
