package cn.welfarezhu.billservice.service;

import cn.welfarezhu.billservice.dto.UnitDTO;
import cn.welfarezhu.billservice.model.WorkingUnit;

public interface UnitService {

    //添加新的unit
    boolean addNewUnit(WorkingUnit unit);

    //修改unit
    boolean modifyUnit(WorkingUnit unit);

    //根据unitId查询Unit
    UnitDTO queryUnitByUnitId(int unitId);

    //根据unitId查询name
    String queryUnitName(int unitId);

    //根据unitId查询principalOd
    int queryPrincipalId(int unitId);

    //根据unitId修改principalId
    boolean modifyPrincipalId(int unit,int principalId);

    boolean countUnitName(String name);
}
