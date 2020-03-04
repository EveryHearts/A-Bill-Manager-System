package cn.welfarezhu.billservice.service.impl;

import cn.welfarezhu.billservice.dto.BillBookDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.enums.BillStatusCode;
import cn.welfarezhu.billservice.exceptions.SQLIResultErrorException;
import cn.welfarezhu.billservice.mapper.*;
import cn.welfarezhu.billservice.model.BillBook;
import cn.welfarezhu.billservice.service.BillBookService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class BillBookServiceImpl implements BillBookService {

    @Resource
    private BillBookMapper bookMapper;
    @Resource
    private BillKindMapper kindMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private SimpleDateFormat dateFormat;
    @Resource
    private BillMapper billMapper;

    @Override
    public int queryVerifyMoney(int bookId) {
        return bookMapper.queryVerifyMoney(bookId);
    }

    @Override
    public boolean setOperatorId(int bookId, int userId) {
        try {
            int count=bookMapper.setOperatorId(bookId,userId);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据薄Operator失败 | book id  : "+bookId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {SQLIResultErrorException.class})
    public boolean addNewBillBook(BillBook billBook) {
        try {
            int count=bookMapper.insert(billBook);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 添加票据薄失败 | book begin code : "+billBook.getBeginCode());
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public BillBookDTO queryBookByBookId(int bookId) {
        BillBook book=bookMapper.selectByPrimaryKey(bookId);
        if (book==null){
            return null;
        }
        return getBillBookDTO(book);
    }

    @Override
    public int queryUnitId(int bookId) {
        return bookMapper.queryUnitId(bookId);
    }

    @Override
    public int queryOperatorId(int bookId) {
        return bookMapper.queryOperatorId(bookId);
    }

    @Override
    public int queryStatusCode(int bookId) {
        return bookMapper.queryStatusCode(bookId);
    }

    @Override
    public boolean modifyStatusCode(int bookId, int statusCode) {
        try {
            int count=bookMapper.updateStatusCode(bookId,statusCode);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据薄状态失败 | book Id : "+bookId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public BillBookDTO queryBookByBillCode(String billCode) {
        int bookId=billMapper.queryBookIdByBillCode(billCode);
        if (bookId==0){
            return null;
        }
        return getBillBookDTO(bookMapper.selectByPrimaryKey(bookId));
    }

    @Override
    public boolean modifyVerifyMoney(int bookId, int verifyMoney) {
        try {
            int count=bookMapper.updateVerifyMoney(bookId,verifyMoney);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据核销金额失败 | book id : "+bookId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyDescription(int bookId, String description) {
        try {
            int count=bookMapper.updateDescription(bookId,description);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据薄备注失败 | book id : "+bookId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public ResultDataMap<BillBookDTO> queryBillBookByUnitId(int unitId, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<BillBook> billBookList=bookMapper.queryBillBookByUnitId(unitId);
        PageInfo pageInfo=new PageInfo(billBookList);
        if (billBookList.size()==0){
            return null;
        }
        //System.out.println(billBookList);
        List<BillBookDTO> bookDTOList=new ArrayList<>();
        for (BillBook billBook:billBookList){
            bookDTOList.add(getBillBookDTO(billBook));
        }
        ResultDataMap<BillBookDTO> resultDataMap=new ResultDataMap<>();
        resultDataMap.setResult(bookDTOList);
        resultDataMap.setPageInfo(pageInfo);
        return resultDataMap;
    }

    @Override
    public ResultDataMap<BillBookDTO> queryBillBookByUnitAndStatus(int unitId, int statusCode, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<BillBook> billBookList=bookMapper.queryBillBookByUnitAndStatus(unitId,statusCode);
        PageInfo pageInfo=new PageInfo(billBookList);
        if (billBookList.size()==0){
            return null;
        }
        //System.out.println(billBookList);
        List<BillBookDTO> bookDTOList=new ArrayList<>();
        for (BillBook billBook:billBookList){
            bookDTOList.add(getBillBookDTO(billBook));
        }
        ResultDataMap<BillBookDTO> resultDataMap=new ResultDataMap<>();
        resultDataMap.setResult(bookDTOList);
        resultDataMap.setPageInfo(pageInfo);
        return resultDataMap;
    }

    @Override
    public List<BillBookDTO> queryVerifyBookByDate(int unitId, String beginTime, String endTime) throws ParseException {
        Date beginDate=dateFormat.parse(beginTime);
        Date endDate=dateFormat.parse(endTime);
        List<BillBook> bookList=bookMapper.queryVerifyBookByDate(unitId,beginDate,endDate);
        if (bookList.size()==0){
            return null;
        }
        List<BillBookDTO> bookDTOList=new ArrayList<>();
        for (BillBook book:bookList){
            bookDTOList.add(getBillBookDTO(book));
        }
        return bookDTOList;
    }

    @Override
    public int countVerifyMoneyByDate(int unitId, String beginTime, String endTime) throws ParseException {
        Date beginDate=dateFormat.parse(beginTime);
        Date endDate=dateFormat.parse(endTime);
        return bookMapper.countVerifyMoneyByDate(unitId,beginDate,endDate);
    }

    @Override
    public int countVerifyMoneyByBookIdList(List<Integer> idList) {
        return bookMapper.countVerifyMoneyByBookIdList(idList);
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

    private BillBookDTO getBillBookDTO(BillBook billBook){
        BillBookDTO bookDTO=new BillBookDTO();
        bookDTO.setBillBookId(billBook.getBillBookId());
        bookDTO.setUnitId(billBook.getUnitId());
        bookDTO.setUnit(unitMapper.queryUnitName(billBook.getUnitId()));
        bookDTO.setOperatorId(billBook.getOperatorId()==null?0:billBook.getOperatorId());
        if (billBook.getOperatorId()!=null){
            bookDTO.setOperator(userMapper.queryUserName(billBook.getOperatorId()));
        }else{
            bookDTO.setOperator("--");
        }
        bookDTO.setKindName(kindMapper.queryKindName(billBook.getBillKindId()));
        bookDTO.setStatus(getStatus(billBook.getStatusCode()));
        bookDTO.setCopies(billBook.getBillCopies());
        bookDTO.setBeginCode(billBook.getBeginCode());
        bookDTO.setEndCode(billBook.getEndCode());
        bookDTO.setTotalMoney(billBook.getTotalMoney());
        bookDTO.setStatusCode(billBook.getStatusCode());
        bookDTO.setVerifyMoney(billBook.getVerifyMoney()==null?0:billBook.getVerifyMoney());
        bookDTO.setDescription(billBook.getDescription()==null?"--":billBook.getDescription());
        bookDTO.setCreateDate(dateFormat.format(billBook.getCreateDate()));
        if (billBook.getModifyDate()!=null){
            bookDTO.setModifyDate(dateFormat.format(billBook.getModifyDate()));
        }
        return bookDTO;
    }
}
