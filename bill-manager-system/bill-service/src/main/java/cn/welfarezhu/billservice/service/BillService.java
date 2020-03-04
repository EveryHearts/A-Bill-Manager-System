package cn.welfarezhu.billservice.service;

import cn.welfarezhu.billservice.dto.BillDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.model.BillInfo;

import java.util.List;

public interface BillService {

    //添加新的Bill
    boolean addNewBill(BillInfo billInfo);

    int queryBillMoney(int billId);

    int queryBookIdByBillId(int billId);

    //根据billId查询operatorId
    int queryOperatorId(int billId);

    //根据billId查询statusCode
    int queryStatusCode(int billId);

    //根据billId修改statusCode
    boolean modifyStatusCode(int billId,int statusCode);

    //根据billId修改description
    boolean modifyDescription(int billId,String desc);

    //根据bookId和statusCode合计billMoney
    int countMoneyByBookIdAndStatusCode(int bookId,int statusCode);

    //根据bookId和statusCode批量修改statusCode
    boolean modifyStatusCodeByBookId(int bookId,int oldStatusCode,int newStatusCode);

    //检查有没有重复的bill（billCode相同）
    boolean isExistsBillCode(String billCode);

    //根据bookId查询Bill
    ResultDataMap<BillDTO> queryBillByBookId(int bookId, int pageNo, int pageSize);

    //根据bookId和statusCode查询Bill
    ResultDataMap<BillDTO> queryBillByBookIdAndStatusCode(int bookId,int statusCode,int pageNo,int pageSize);

    boolean setOperatorId(int billId,int userId);

    boolean setOperatorIdByBookId(int bookId,int operatorId);
}
