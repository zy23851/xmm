package com.pn.mapper;

import com.pn.entity.Role;
import com.pn.entity.User;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {

    //查询所有角色的方法
    List<Role> findAllRole();

    //查询指定用户下的所有角色
    List<Role> findUserRoleByUid(Integer userId);

    //根据角色名查询角色id的方法
    Integer findRoleIdByName(String roleName);

    //查询角色行数的方法
    Integer findRoleRowCount(Role role);
    //分页查询角色的方法
    List<Role> findRolePage(@Param("role") Role role,@Param("page") Page page);

    //根据角色名称或角色代码查询角色的方法
    Role findRoleByNameOrCode(String roleName,String roleCode);
    //添加角色的方法
    int insertRole(Role role);
    //根据角色id修改角色状态的方法
    int setRoleStateByRid(Integer roleId,String roleState);

    //根据角色id删除角色 -- 这个是真删，没有is_delete字段
    int removeRoleById(Integer roleId);

    //根据角色id修改角色描述的方法
    int setDescByRid(Role role);

}
