package cn.welfarezhu.billservice.service.impl;

import cn.welfarezhu.billservice.dto.RecordDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.enums.ObjectSortCode;
import cn.welfarezhu.billservice.enums.OperationCode;
import cn.welfarezhu.billservice.exceptions.SQLIResultErrorException;
import cn.welfarezhu.billservice.mapper.OperationRecordMapper;
import cn.welfarezhu.billservice.mapper.UnitMapper;
import cn.welfarezhu.billservice.mapper.UserMapper;
import cn.welfarezhu.billservice.model.OperationRecord;
import cn.welfarezhu.billservice.service.RecordService;
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
public class RecordServiceImpl implements RecordService {

    @Resource
    private OperationRecordMapper recordMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    SimpleDateFormat dateFormat;

    @Override
    public boolean addNewRecord(int unitId, int userId, int operationSort, int objectSort, int objectId) {
        OperationRecord record=new OperationRecord();
        record.setUnitId(unitId);
        record.setUserId(userId);
        record.setOperationSort(operationSort);
        record.setObjectId(objectId);
        record.setObjectSort(objectSort);
        record.setOperationDate(new Date());
        try {
            int count=recordMapper.insert(record);
            if (count != 1 ){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 添加操作记录失败 | record is : "+record);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public ResultDataMap<RecordDTO> queryRecordByOperationSort(int operationSort, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<OperationRecord> recordList=recordMapper.queryRecordByOperationSort(operationSort);
        PageInfo pageInfo=new PageInfo(recordList);
        if (recordList.size()==0){
            return null;
        }
        List<RecordDTO> recordDTOList=new ArrayList<>();
        for (OperationRecord operationRecord:recordList){
            recordDTOList.add(getRecordDTO(operationRecord));
        }
        ResultDataMap<RecordDTO> recordDTOResultDataMap=new ResultDataMap<>();
        recordDTOResultDataMap.setResult(recordDTOList);
        recordDTOResultDataMap.setPageInfo(pageInfo);
        return recordDTOResultDataMap;
    }

    @Override
    public ResultDataMap<RecordDTO> queryRecordByUnitId(int unitId, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<OperationRecord> recordList=recordMapper.queryRecordByUnitId(unitId);
        PageInfo pageInfo=new PageInfo(recordList);
        if (recordList.size()==0){
            return null;
        }
        List<RecordDTO> recordDTOList=new ArrayList<>();
        for (OperationRecord operationRecord:recordList){
            recordDTOList.add(getRecordDTO(operationRecord));
        }
        ResultDataMap<RecordDTO> recordDTOResultDataMap=new ResultDataMap<>();
        recordDTOResultDataMap.setResult(recordDTOList);
        recordDTOResultDataMap.setPageInfo(pageInfo);
        return recordDTOResultDataMap;
    }

    @Override
    public ResultDataMap<RecordDTO> queryRecordByUserId(int userId, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<OperationRecord> recordList=recordMapper.queryRecordByUserId(userId);
        PageInfo pageInfo=new PageInfo(recordList);
        if (recordList.size()==0){
            return null;
        }
        List<RecordDTO> recordDTOList=new ArrayList<>();
        for (OperationRecord operationRecord:recordList){
            recordDTOList.add(getRecordDTO(operationRecord));
        }
        ResultDataMap<RecordDTO> recordDTOResultDataMap=new ResultDataMap<>();
        recordDTOResultDataMap.setResult(recordDTOList);
        recordDTOResultDataMap.setPageInfo(pageInfo);
        return recordDTOResultDataMap;
    }

    @Override
    public ResultDataMap<RecordDTO> queryRecordByDateAndUnitId(int unitId, String begin, String end, int pageNo, int pageSize) throws ParseException {
        PageHelper.startPage(pageNo,pageSize);
        List<OperationRecord> recordList=recordMapper.queryRecordByDateAndUnitId(unitId,dateFormat.parse(begin),dateFormat.parse(end));
        PageInfo pageInfo=new PageInfo(recordList);
        if (recordList.size()==0){
            return null;
        }
        List<RecordDTO> recordDTOList=new ArrayList<>();
        for (OperationRecord operationRecord:recordList){
            recordDTOList.add(getRecordDTO(operationRecord));
        }
        ResultDataMap<RecordDTO> recordDTOResultDataMap=new ResultDataMap<>();
        recordDTOResultDataMap.setResult(recordDTOList);
        recordDTOResultDataMap.setPageInfo(pageInfo);
        return recordDTOResultDataMap;
    }

    @Override
    public ResultDataMap<RecordDTO> queryRecordByDateAndUserId(int userId, String begin, String end, int pageNo, int pageSize) throws ParseException{
        PageHelper.startPage(pageNo,pageSize);
        List<OperationRecord> recordList=recordMapper.queryRecordByDateAndUserId(userId,dateFormat.parse(begin),dateFormat.parse(end));
        PageInfo pageInfo=new PageInfo(recordList);
        if (recordList.size()==0){
            return null;
        }
        List<RecordDTO> recordDTOList=new ArrayList<>();
        for (OperationRecord operationRecord:recordList){
            recordDTOList.add(getRecordDTO(operationRecord));
        }
        ResultDataMap<RecordDTO> recordDTOResultDataMap=new ResultDataMap<>();
        recordDTOResultDataMap.setResult(recordDTOList);
        recordDTOResultDataMap.setPageInfo(pageInfo);
        return recordDTOResultDataMap;
    }

    private String getOperation(int operationSort){
        String opt;
        switch (operationSort){
            case 1:
                opt= OperationCode.ADD_NEW_ROLE.operation();
                break;
            case 2:
                opt=OperationCode.MODIFY_USER_INFO.operation();
                break;
            case 3:
                opt=OperationCode.MODIFY_USER_PASS.operation();
                break;
            case 4:
                opt=OperationCode.BANNED_ROLE.operation();
                break;
            case 5:
                opt=OperationCode.LOGOUT_ROLE.operation();
                break;
            case 6:
                opt=OperationCode.ADD_NEW_BILL_BOOK.operation();
                break;
            case 7:
                opt=OperationCode.RECEIVE.operation();
                break;
            case 8:
                opt=OperationCode.USED.operation();
                break;
            case 9:
                opt=OperationCode.CANCELED.operation();
                break;
            default:
                opt=OperationCode.BANNED_BILL.operation();
        }
        return opt;
    }

    private String getObjectSort(int objSort){
        String sort;
        switch (objSort){
            case 1:
                sort= ObjectSortCode.BILL.sort();
                break;
            case 2:
                sort=ObjectSortCode.BILL_BOOK.sort();
                break;
            case 3:
                sort=ObjectSortCode.UNIT.sort();
                break;
            default:
                sort=ObjectSortCode.USER.sort();
        }
        return sort;
    }

    private RecordDTO getRecordDTO(OperationRecord record){
        RecordDTO recordDTO=new RecordDTO();
        recordDTO.setRecordId(record.getRecordId());
        recordDTO.setUserId(record.getUserId());
        recordDTO.setUser(userMapper.queryUserName(record.getUserId()));
        recordDTO.setUnitId(record.getUnitId());
        recordDTO.setUnit(unitMapper.queryUnitName(record.getUnitId()));
        recordDTO.setOperation(getOperation(record.getOperationSort()));
        recordDTO.setObjectSort(getObjectSort(record.getObjectSort()));
        recordDTO.setObjectId(record.getObjectId());
        recordDTO.setOperationDate(dateFormat.format(record.getOperationDate()));
        return recordDTO;
    }
}
