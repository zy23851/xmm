package com.pn.service;

import com.pn.dto.AssignAuthDto;
import com.pn.entity.Auth;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.page.Page;

import java.util.List;

public interface RoleService {

    //查询所有角色信息
    List<Role> queryAllRole();
    //查询指定用户的角色信息
    List<Role> queryUserRoleByUid(Integer userId);

    //分页查询角色的业务方法
    Page queryRolePage(Role role,Page page);

    //添加角色的业务方法
    Result saveRole(Role role);
    //根据角色id修改角色状态的方法
    Result setRoleState(Role role);

    //根据角色id删除角色
    Result deleteRoleById(Integer roleId);

    //根据角色id查询角色分配的菜单权限的id
    List<Integer> queryRoleAuthIds(Integer roleId);

    //给角色分配权限的业务
    void saveRoleAuth(AssignAuthDto assignAuthDto);

    //根据角色id修改角色描述的方法
    Result setRoleByRid(Role role);

}
