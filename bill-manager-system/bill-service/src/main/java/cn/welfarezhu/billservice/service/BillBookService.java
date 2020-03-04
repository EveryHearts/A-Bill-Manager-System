package cn.welfarezhu.billservice.service;

import cn.welfarezhu.billservice.dto.BillBookDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.model.BillBook;

import java.text.ParseException;
import java.util.List;

public interface BillBookService {

    //添加新的BillBook
    boolean addNewBillBook(BillBook billBook);

    BillBookDTO queryBookByBookId(int bookId);

    //根据boolId查询unitId
    int queryUnitId(int bookId);

    //根据bookId查询operatorId
    int queryOperatorId(int bookId);

    //根据bookId查询statusCode
    int queryStatusCode(int bookId);

    //根据bookId修改statusCode
    boolean modifyStatusCode(int bookId,int statusCode);

    //根据bookId修改verifyMoney
    boolean modifyVerifyMoney(int bookId,int verifyMoney);

    //根据bookId修改description
    boolean modifyDescription(int bookId,String description);

    List<BillBookDTO> queryVerifyBookByDate(int unitId,String beginTime ,String endTime) throws ParseException;

    //根据unitId查询BillBook
    ResultDataMap<BillBookDTO> queryBillBookByUnitId(int unitId,int pageNo,int pageSize);

    //根据unitId和statusCode查询BillBook
    ResultDataMap<BillBookDTO> queryBillBookByUnitAndStatus(int unitId,int statusCode,int pageNo,int pageSize);

    //根据unitId和日期间隔查询核销金额总计
    int countVerifyMoneyByDate(int unitId, String beginTime,String endTime) throws ParseException;

    //根据bookId List查询核销金额总计
    int countVerifyMoneyByBookIdList(List<Integer> idList);

    int queryVerifyMoney(int bookId);

    boolean setOperatorId(int bookId,int userId);

    BillBookDTO queryBookByBillCode(String billCode);
}
