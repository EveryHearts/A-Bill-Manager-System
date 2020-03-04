package cn.welfarezhu.billservice.service.impl;

import cn.welfarezhu.billservice.dto.UserDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.enums.RoleCode;
import cn.welfarezhu.billservice.enums.RoleStatusCode;
import cn.welfarezhu.billservice.exceptions.NullResultException;
import cn.welfarezhu.billservice.exceptions.SQLIResultErrorException;
import cn.welfarezhu.billservice.mapper.UnitMapper;
import cn.welfarezhu.billservice.mapper.UserMapper;
import cn.welfarezhu.billservice.model.UserInfo;
import cn.welfarezhu.billservice.service.UserService;
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
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private SimpleDateFormat dateFormat;

    @Override
    public List<Integer> queryUserIdListByUnitId(int unitId) {
        return userMapper.queryUserIdListByUnitId(unitId);
    }

    @Override
    public boolean addNewUser(UserInfo userInfo) {
        try{
            int count=userMapper.insert(userInfo);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+"| 添加用户失败，结果异常！| user name : "+userInfo.getUserName());
            }
        }catch (SQLIResultErrorException e){
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public UserDTO queryUserByUserId(int userId) {
        UserInfo userInfo=userMapper.selectByPrimaryKey(userId);
        return getUserDTO(userInfo);
    }

    @Override
    public boolean modifyUser(UserInfo userInfo) {
        try {
            int count=userMapper.updateByPrimaryKeySelective(userInfo);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+"| 修改用户失败，结果异常!| user id : "+userInfo.getUserId());
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public UserDTO queryUserByAccount(String account) {
        try {
            UserInfo userInfo=userMapper.queryAccountAndPassword(account);
            if (userInfo==null){
                throw new NullResultException(dateFormat.format(new Date())+" | 根据账号查询的用户不存在！| user account : "+account);
            }
            return getUserDTO(userInfo);
        } catch (NullResultException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    @Override
    public boolean modifyUserPassword(int userId, String password) {
        try {
            int count=userMapper.updatePassword(userId,password);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改用户密码失败! | user id : "+userId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public String queryUserPassword(int userId) {
        return userMapper.queryPassword(userId);
    }

    @Override
    public boolean modifyStatusCode(int userId, int statusCode) {
        try {
            int count=userMapper.updateStatusCode(userId,statusCode);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改用户状态失败！| user id : "+userId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public int queryRoleCode(int userId) {
        return userMapper.queryRoleCode(userId);
    }

    @Override
    public int queryStatusCode(int userId) {
        return userMapper.queryStatusCode(userId);
    }

    @Override
    public ResultDataMap<UserDTO> queryUserList(int uniId, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<UserInfo> userInfoList=userMapper.queryUserByUnitId(uniId);
        PageInfo pageInfo=new PageInfo(userInfoList);
        if (userInfoList.size()==0){
            return null;
        }
        List<UserDTO> userList=new ArrayList<>();
        for (UserInfo userInfo:userInfoList){
            userList.add(getUserDTO(userInfo));
        }
        ResultDataMap<UserDTO> resultDataMap=new ResultDataMap<>();
        resultDataMap.setResult(userList);
        resultDataMap.setPageInfo(pageInfo);
        return resultDataMap;
    }

    @Override
    public boolean isAccountExists(String account) {
        System.out.println(userMapper.queryAccountCount(account));
        return userMapper.queryAccountCount(account)!=0;
    }

    @Override
    public ResultDataMap<UserDTO> queryUserByUnitId(int unitId, int statusCode,int pageNo,int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<UserInfo> userInfoList=userMapper.queryUserIdByUnitId(unitId,statusCode);
        PageInfo pageInfo=new PageInfo(userInfoList);
        if (userInfoList.size()==0){
            return null;
        }
        List<UserDTO> userList=new ArrayList<>();
        for (UserInfo userInfo:userInfoList){
            userList.add(getUserDTO(userInfo));
        }
        ResultDataMap<UserDTO> resultDataMap=new ResultDataMap<>();
        resultDataMap.setResult(userList);
        resultDataMap.setPageInfo(pageInfo);
        return resultDataMap;
    }

    private RoleCode getRoleCode(int code){
        if (code == 3){
            return RoleCode.REGISTER;
        }
        if (code == 1){
            return RoleCode.OPERATOR;
        }
        if (code == 2){
            return RoleCode.MONITOR;
        }
        return RoleCode.MANAGER;
    }

    private String getRoleValue(int code){
        if (code == 3){
            return RoleCode.REGISTER.roleValue();
        }
        if (code == 1){
            return RoleCode.OPERATOR.roleValue();
        }
        if (code == 2){
            return RoleCode.MONITOR.roleValue();
        }
        return RoleCode.MANAGER.roleValue();
    }

    private String getStatus(int statusCode){
        if (statusCode == 1){
            return RoleStatusCode.BAN.status();
        }
        if (statusCode == 2){
            return RoleStatusCode.LOGOUT.status();
        }
        return RoleStatusCode.NORMAL.status();
    }

    private UserDTO getUserDTO(UserInfo userInfo){
        UserDTO userDTO=new UserDTO();
        userDTO.setUserId(userInfo.getUserId());
        userDTO.setUnitId(userInfo.getUnitId());
        userDTO.setUnit(unitMapper.queryUnitName(userInfo.getUnitId()));
        userDTO.setName(userInfo.getUserName());
        userDTO.setRoleNum(userInfo.getRoleCode());
        userDTO.setRoleCode(getRoleCode(userInfo.getRoleCode()));
        userDTO.setRole(getRoleCode(userInfo.getRoleCode()).roleValue());
        userDTO.setStatusCode(userInfo.getStatusCode());
        userDTO.setStatus(getStatus(userInfo.getStatusCode()));
        userDTO.setAddress(userInfo.getAddress()==null?"--":userInfo.getAddress());
        userDTO.setAge(userInfo.getAge()==null?0:userInfo.getAge());
        userDTO.setGender(userInfo.getGender()==0?"男":"女");
        userDTO.setPhone(userInfo.getPhone());
        userDTO.setRoleValue(getRoleValue(userInfo.getRoleCode()));
        userDTO.setDescription(userInfo.getDescription()==null?"--":userInfo.getDescription());
        userDTO.setCreateDate(dateFormat.format(userInfo.getCreateDate()));
        return userDTO;
    }
}
