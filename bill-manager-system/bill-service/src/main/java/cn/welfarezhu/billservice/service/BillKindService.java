package cn.welfarezhu.billservice.service;

import cn.welfarezhu.billservice.dto.BillKindDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.model.BillKind;

public interface BillKindService {

    //添加新的BillKind
    boolean addNewBillKind(BillKind billKind);

    //根据kindId修改statusCode
    boolean modifyStatusCode(int kindId,int statusCode);

    //根据unitId查询BillKind
    ResultDataMap<BillKindDTO> queryBillKindByUnitId(int unitId, int pageNo, int pageSize);

    //查询是否存在kindName
    boolean isKindNameExists(int unitId,String kindName);
}
