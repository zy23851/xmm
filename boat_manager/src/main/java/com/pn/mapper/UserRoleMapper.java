package com.pn.mapper;

import com.pn.entity.UserRole;

public interface UserRoleMapper {

    //根据用户id删除用户已经分配的用户角色关系
    int removeUserRoleByUid(Integer userId);

    //添加用户角色关系
    int insertUserRole(UserRole userRole);
}
