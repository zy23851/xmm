package com.pn.service;

import com.pn.dto.AssignRoleDto;
import com.pn.entity.Result;
import com.pn.entity.User;
import com.pn.page.Page;

import java.util.List;

public interface UserService {
    //根据账号查询用户信息
    User queryUserByCode(String userCode);
    //分页查询用户信息
    Page queryUserByPage(Page page,User user);
    //添加用户信息方法
    Result saveUser(User user);
    //启用或禁用用户状态
    Result setUserState(User user);


    //给用户分配角色的业务方法
    void assignRole(AssignRoleDto assignRoleDto);
    //删除用户的业务
    Result removeUserByIds(List<Integer> userIdList);
    //修改用户信息
    Result setUserById(User user);
    //根据用户id重置密码
    Result setPwdById(Integer userId);
}
