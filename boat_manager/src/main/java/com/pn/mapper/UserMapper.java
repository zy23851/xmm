package com.pn.mapper;

/*
       user_info的mapper接口
 */

import com.pn.entity.User;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper {

    //根据账号查询用户信息
    User findUserByCode(String userCode);
    //获取用户行数 -- 总行数  --  带条件之后的行数
    Integer findUserRowCount(User user);
    //分页查询用户的方法
    List<User> findUserByPage(@Param("page") Page page,@Param("user") User user);
    //添加用户信息的方法
    int insertUser(User user);
    //启用或禁用用户状态
    int serStateByUid(Integer userId,String userState);
    //根据用户ids修改用户删除状态的方法
    int setIsDeleteByUids(List<Integer> userIdList);
    //修改用户
    int setUserNameByUid(User user);
    //根据用户id重置密码
    int setPwdByUid(Integer userId,String userPwd);
}
