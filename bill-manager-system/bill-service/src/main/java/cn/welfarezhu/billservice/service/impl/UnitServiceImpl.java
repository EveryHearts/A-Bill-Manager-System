package cn.welfarezhu.billservice.service.impl;

import cn.welfarezhu.billservice.dto.UnitDTO;
import cn.welfarezhu.billservice.exceptions.SQLIResultErrorException;
import cn.welfarezhu.billservice.mapper.UnitMapper;
import cn.welfarezhu.billservice.mapper.UserMapper;
import cn.welfarezhu.billservice.model.WorkingUnit;
import cn.welfarezhu.billservice.service.UnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional(rollbackFor = {Exception.class})
public class UnitServiceImpl implements UnitService {

    @Resource
    private UnitMapper unitMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SimpleDateFormat dateFormat;

    @Override
    public boolean addNewUnit(WorkingUnit unit) {
        try {
            int count=unitMapper.insert(unit);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 添加单位失败，出现异常！| unit name : "+unit.getUnitName());
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyUnit(WorkingUnit unit) {
        try {
            int count=unitMapper.updateByPrimaryKeySelective(unit);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改单位信息失败！| unit id : "+unit.getUnitId());
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public UnitDTO queryUnitByUnitId(int unitId) {
        return getUnitDTO(unitMapper.selectByPrimaryKey(unitId));
    }

    @Override
    public String queryUnitName(int unitId) {
        return unitMapper.queryUnitName(unitId);
    }

    @Override
    public int queryPrincipalId(int unitId) {
        return unitMapper.queryPrincipalId(unitId);
    }

    @Override
    public boolean modifyPrincipalId(int unitId, int principalId) {
        try {
            int count=unitMapper.updatePrincipalId(unitId,principalId);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改单位负责人ID失败 | unit Id : "+unitId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean countUnitName(String name) {
        return unitMapper.countUnitName(name)==0;
    }

    private UnitDTO getUnitDTO(WorkingUnit unit){
        UnitDTO unitDTO=new UnitDTO();
        unitDTO.setUnitId(unit.getUnitId());
        unitDTO.setUnitName(unit.getUnitName());
        unitDTO.setPrincipalId(unit.getUnitPrincipalId());
        unitDTO.setPrincipalName(userMapper.queryUserName(unit.getUnitPrincipalId()));
        unitDTO.setAddress(unit.getUnitAddress()==null?"--":unit.getUnitAddress());
        unitDTO.setPhone(unit.getUnitPhone()==null?"--":unit.getUnitPhone());
        unitDTO.setDescription(unit.getUnitDescription()==null?"--":unit.getUnitDescription());
        unitDTO.setRegisterDate(dateFormat.format(unit.getRegisterDate()));
        return unitDTO;
    }
}
