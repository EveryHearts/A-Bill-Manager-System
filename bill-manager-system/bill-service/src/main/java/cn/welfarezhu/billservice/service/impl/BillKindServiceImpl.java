package cn.welfarezhu.billservice.service.impl;

import cn.welfarezhu.billservice.dto.BillKindDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.exceptions.SQLIResultErrorException;
import cn.welfarezhu.billservice.mapper.BillKindMapper;
import cn.welfarezhu.billservice.mapper.UnitMapper;
import cn.welfarezhu.billservice.model.BillKind;
import cn.welfarezhu.billservice.service.BillKindService;
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
public class BillKindServiceImpl implements BillKindService {

    @Resource
    private BillKindMapper billKindMapper;
    @Resource
    private SimpleDateFormat dateFormat;
    @Resource
    private UnitMapper unitMapper;

    @Override
    public boolean isKindNameExists(int unitId, String kindName) {
        return billKindMapper.countKindName(unitId,kindName)!=0;
    }

    @Override
    public boolean addNewBillKind(BillKind billKind) {
        try {
            int count=billKindMapper.insert(billKind);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 添加票据种类失败 | kind name : "+billKind.getKindName());
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyStatusCode(int kindId, int statusCode) {
        try {
            int count=billKindMapper.updateStatusCode(kindId,statusCode);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改票据种类状态失败 | kind id : "+kindId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public ResultDataMap<BillKindDTO> queryBillKindByUnitId(int unitId, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<BillKind> billKindList=billKindMapper.queryBillKindByUnitId(unitId);
        PageInfo pageInfo=new PageInfo(billKindList);
        if (billKindList.size()==0){
            return null;
        }
        List<BillKindDTO> kindDTOList=new ArrayList<>();
        for (BillKind kind:billKindList){
            kindDTOList.add(getKindDTO(kind));
        }
        ResultDataMap<BillKindDTO> resultDataMap=new ResultDataMap<>();
        resultDataMap.setResult(kindDTOList);
        resultDataMap.setPageInfo(pageInfo);
        return resultDataMap;
    }

    private BillKindDTO getKindDTO(BillKind billKind){
        BillKindDTO kindDTO=new BillKindDTO();
        kindDTO.setBillKindId(billKind.getBillKindId());
        kindDTO.setUnitId(billKind.getUnitId());
        kindDTO.setUnit(unitMapper.queryUnitName(billKind.getUnitId()));
        kindDTO.setKindName(billKind.getKindName());
        kindDTO.setStatusCode(billKind.getStatusCode());
        kindDTO.setCreateDate(dateFormat.format(billKind.getCreateDate()));
        return kindDTO;
    }
}
