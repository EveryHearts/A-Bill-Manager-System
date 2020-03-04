package cn.welfarezhu.billservice.service;

import cn.welfarezhu.billservice.dto.UserDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.model.UserInfo;

import java.util.List;

public interface UserService {

    //注册或者添加User
    boolean addNewUser(UserInfo userInfo);

    //根据userId查询用户信息
    UserDTO queryUserByUserId(int userId);

    //修改User
    boolean modifyUser(UserInfo userInfo);

    //根据account查询用户信息
    UserDTO queryUserByAccount(String account);

    boolean isAccountExists(String account);

    //根据userId修改账号密码
    boolean modifyUserPassword(int userId,String password);

    //根据userId查询password
    String queryUserPassword(int userId);

    //根据userId修改statusCode
    boolean modifyStatusCode(int userId,int statusCode);

    //根据userId查询roleCode
    int queryRoleCode(int userId);

    //根据userId查询statusCode
    int queryStatusCode(int userId);

    //根据unitId查询user List
    ResultDataMap<UserDTO> queryUserList(int uniId,int pageNo,int pageSize);

    //根据unitId和statusCode查询user
    ResultDataMap<UserDTO> queryUserByUnitId(int unitId,int statusCode,int pageNo,int pageSize);

    List<Integer> queryUserIdListByUnitId(int unitId);
}
