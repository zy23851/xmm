package com.pn.mapper;

import com.pn.entity.RoleAuth;

import java.util.List;

public interface RoleAuthMapper {

    //根据角色id删除角色权限
    int removeRoleAuthByRid(Integer roleId);

    //根据角色id查询角色分配的菜单权限的id
    List<Integer> findAuthIdsByRid(Integer roleId);


    //添加角色权限关系的方法
    int insertRoleAuth(RoleAuth roleAuth);
}
