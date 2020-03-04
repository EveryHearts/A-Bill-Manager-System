package cn.welfarezhu.billservice.mapper;

import cn.welfarezhu.billservice.model.UserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<UserInfo> {
    @Select(value = "select * from user_info where account = #{account}")
    @Results(id = "userMap",value = {
            @Result(column = "user_id",property = "userId"),
            @Result(column = "unit_id",property = "unitId"),
            @Result(column = "role_code",property = "roleCode"),
            @Result(column = "user_name",property = "userName"),
            @Result(column = "phone",property = "phone"),
            @Result(column = "address",property = "address"),
            @Result(column = "age",property = "age"),
            @Result(column = "gender",property = "gender"),
            @Result(column = "account",property = "account"),
            @Result(column = "password",property = "password"),
            @Result(column = "status_code",property = "statusCode"),
            @Result(column = "create_date",property = "createDate" ,jdbcType = JdbcType.DATE),
            @Result(column = "modify_date",property = "modifyDate", jdbcType = JdbcType.DATE),
            @Result(column = "description",property = "description")
    })
    UserInfo queryAccountAndPassword(String account);   //根据账号查询用户信息

    //按照ID查询姓名
    @Select(value = "select user_name from user_info where user_id = #{userId}")
    String queryUserName(int userId);

    //根据ID修改账号密码
    @Update(value = "update user_info set modify_date = now() , password = #{password} where user_id = #{userId}")
    int updatePassword(@Param("userId") int userId,@Param("password") String password);

    //根据ID修改状态
    @Update(value = "update user_info set modify_date = now() , status_code = #{statusCode} where user_id = #{userId}")
    int updateStatusCode(@Param("userId") int userId,@Param("statusCode") int statusCode);

    //根据ID查询roleCode
    @Select(value = "select role_code from user_info where user_id = #{userId}")
    int queryRoleCode(int userId);

    //根据ID查询状态
    @Select(value = "select status_code from user_info where user_id = #{userId}")
    int queryStatusCode(int userId);

    //根据unitId查询user List
    @Select(value = "select * from user_info where unit_id = #{unitId} order by create_date desc")
    List<UserInfo> queryUserByUnitId(int unitId);

    //根据ID查询password
    @Select(value = "select password from user_info where user_id = #{userId}")
    String queryPassword(int userId);

    @Select(value = "select count(*) from user_info where account = #{account}")
    int queryAccountCount(String account);

    //根据unitId和statusCode查询userId
    @Select(value = "select * from user_info where unit_id = #{unitId} and status_code = #{statusCode} order by create_date desc")
    List<UserInfo> queryUserIdByUnitId(@Param("unitId") int unitId,@Param("statusCode") int roleCode);

    @Select(value = "select user_id from user_info where unit_id = #{unitId}")
    List<Integer> queryUserIdListByUnitId(int unitId);
}
