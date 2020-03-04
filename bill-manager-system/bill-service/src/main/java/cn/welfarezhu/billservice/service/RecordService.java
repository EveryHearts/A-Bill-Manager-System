package cn.welfarezhu.billservice.service;

import cn.welfarezhu.billservice.dto.RecordDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;

import java.text.ParseException;
import java.util.List;

public interface RecordService {

    //添加Record
    boolean addNewRecord(int unitId,int userId,int operationSort,int objectSort,int objectId);

    ResultDataMap<RecordDTO> queryRecordByOperationSort(int operationSort,int pageNo,int pageSize);

    //根据unitId查询Record
    ResultDataMap<RecordDTO> queryRecordByUnitId(int unitId, int pageNo, int pageSize);

    //根据userId查询Record
    ResultDataMap<RecordDTO> queryRecordByUserId(int userId,int pageNo,int pageSize);

    //根据unitId和日期间隔查询Record
    ResultDataMap<RecordDTO> queryRecordByDateAndUnitId(int unitId,String begin,String end,int pageNo,int pageSize) throws ParseException;

    //根据userId和日期间隔查询Record
    ResultDataMap<RecordDTO> queryRecordByDateAndUserId(int userId,String begin,String end,int pageNo,int pageSize) throws ParseException;
}
