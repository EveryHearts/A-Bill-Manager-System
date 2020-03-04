package cn.welfarezhu.billservice.service.impl;

import cn.welfarezhu.billservice.dto.BillDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.enums.BillStatusCode;
import cn.welfarezhu.billservice.exceptions.SQLIResultErrorException;
import cn.welfarezhu.billservice.mapper.BillBookMapper;
import cn.welfarezhu.billservice.mapper.BillKindMapper;
import cn.welfarezhu.billservice.mapper.BillMapper;
import cn.welfarezhu.billservice.mapper.UserMapper;
import cn.welfarezhu.billservice.model.BillInfo;
import cn.welfarezhu.billservice.service.BillService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class BillServiceImpl implements BillService {

    @Resource
    private BillMapper billMapper;
    @Resource
    private BillKindMapper kindMapper;
    @Resource
    private BillBookMapper bookMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SimpleDateFormat dateFormat;

    @Override
    public int queryBillMoney(int billId) {
        return billMapper.queryBillMoney(billId);
    }

    @Override
    public boolean setOperatorId(int billId, int userId) {
        try {
            int count=billMapper.setOperatorId(billId,userId);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据Operator失败 | bill id : "+billId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean addNewBill(BillInfo billInfo) {
        try {
            int count=billMapper.insert(billInfo);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 添加票据失败 | bill code : "+billInfo.getBillCode());
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public int queryBookIdByBillId(int billId) {
        return billMapper.queryBookIdByBillId(billId);
    }

    @Override
    public int queryOperatorId(int billId) {
        return billMapper.queryOperatorId(billId);
    }

    @Override
    public int queryStatusCode(int billId) {
        return billMapper.queryStatusCode(billId);
    }

    @Override
    public boolean modifyStatusCode(int billId, int statusCode) {
        try {
            int count=billMapper.updateStatusCode(billId,statusCode);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据状态失败 | bill id : "+billId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyDescription(int billId, String desc) {
        try {
            int count=billMapper.updateDescription(billId,desc);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据备注失败 | bill id : "+billId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean setOperatorIdByBookId(int bookId, int operatorId) {
        List<BillInfo> billIdList=billMapper.queryBillByBookId(bookId);
        if (billIdList.size()==0){
            return true;
        }
        int count=0;
        try {
            for (BillInfo bill:billIdList){
                count+=billMapper.setOperatorId(bookId,operatorId);
            }
            if (count != billIdList.size()){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据Operator失败 | bookId id : "+bookId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public int countMoneyByBookIdAndStatusCode(int bookId, int statusCode) {
        return billMapper.countMoneyByBookIdAnfStatusCode(bookId,statusCode);
    }

    @Override
    public boolean modifyStatusCodeByBookId(int bookId, int oldStatusCode, int newStatusCode) {
        List<Integer> idList=billMapper.queryBillIdByBookIdAndStatusCode(bookId,oldStatusCode);
        if (idList.size()==0){
            return true;
        }
        try {
            int count=billMapper.updateStatusByBillIdList(idList,newStatusCode);
            if (count != idList.size()){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据状态失败 | bill id list : "+idList);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean isExistsBillCode(String billCode) {
        int count=billMapper.countBillByBillCode(billCode);
        return count!=0;
    }

    @Override
    public ResultDataMap<BillDTO> queryBillByBookId(int bookId, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<BillInfo> billInfoList=billMapper.queryBillByBookId(bookId);
        PageInfo pageInfo=new PageInfo(billInfoList);
        if (billInfoList.size()==0){
            return null;
        }
        List<BillDTO> billDTOList=new ArrayList<>();
        for (BillInfo billInfo:billInfoList){
            billDTOList.add(getBillDTO(billInfo));
        }
        ResultDataMap<BillDTO> resultDataMap=new ResultDataMap<>();
        resultDataMap.setResult(billDTOList);
        resultDataMap.setPageInfo(pageInfo);
        return resultDataMap;
    }

    @Override
    public ResultDataMap<BillDTO> queryBillByBookIdAndStatusCode(int bookId, int statusCode,int pageNo,int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<BillInfo> billInfoList=billMapper.queryBillByBookIdAndStatusCode(bookId,statusCode);
        PageInfo pageInfo=new PageInfo(billInfoList);
        if (billInfoList.size()==0){
            return null;
        }
        List<BillDTO> billDTOList=new ArrayList<>();
        for (BillInfo billInfo:billInfoList){
            billDTOList.add(getBillDTO(billInfo));
        }
        ResultDataMap<BillDTO> resultDataMap=new ResultDataMap<>();
        resultDataMap.setResult(billDTOList);
        resultDataMap.setPageInfo(pageInfo);
        return resultDataMap;
    }

    private String getStatus(int statusCode){
        String status;
        switch (statusCode){
            case 1:
                status= BillStatusCode.RECEIVE.status();
                break;
            case 2:
                status=BillStatusCode.USED.status();
                break;
            case 3:
                status=BillStatusCode.WAIT_VERIFY.status();
                break;
            case 4:
                status=BillStatusCode.CANCEL.status();
                break;
            case 5:
                status=BillStatusCode.BANNING.status();
                break;
            case 6:
                status=BillStatusCode.BANNED.status();
                break;
            default:
                status=BillStatusCode.PROVIDE.status();
        }
        return status;
    }

    private BillDTO getBillDTO(BillInfo billInfo){
        BillDTO bill=new BillDTO();
        bill.setBillId(billInfo.getBillId());
        bill.setBillKind(kindMapper.queryKindName(billInfo.getBillKindId()));
        bill.setBillBookId(billInfo.getBillBookId());
        bill.setOperatorId(billInfo.getOperatorId()==null?0:billInfo.getOperatorId());
        if (billInfo.getOperatorId()!=null){
            bill.setOperator(userMapper.queryUserName(billInfo.getOperatorId()));
        }else{
            bill.setOperator("--");
        }
        bill.setCode(billInfo.getBillCode());
        bill.setMoney(billInfo.getBillMoney());
        bill.setStatusCode(billInfo.getStatusCode());
        bill.setStatus(getStatus(billInfo.getStatusCode()));
        bill.setDescription(billInfo.getDescription()==null?"--":billInfo.getDescription());
        bill.setCreateDate(dateFormat.format(billInfo.getCreateDate()));
        if (billInfo.getModifyDate()!=null) {
            bill.setModifyDate(dateFormat.format(billInfo.getModifyDate()));
        }
        return bill;
    }
}
