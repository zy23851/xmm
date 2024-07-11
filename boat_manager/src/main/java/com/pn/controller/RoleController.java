package com.pn.controller;

import com.pn.dto.AssignAuthDto;
import com.pn.entity.Auth;
import com.pn.entity.CurrentUser;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.page.Page;
import com.pn.service.RoleService;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    // 查询所有角色    的url接口  -- /role/role-list
    // 这个请求是将所有的角色信息展示到分配角色的弹出框里的
    @RequestMapping("/role-list")
    public Result roleList(){
        List<Role> roles = roleService.queryAllRole();
        return Result.ok(roles);
    }

    @RequestMapping("/role-page-list")
    public Result roleListPage(Role role, Page page){
        page = roleService.queryRolePage(role, page);
        return Result.ok(page);
    }

    @Autowired
    private TokenUtils tokenUtils;

    //添加角色  /role/role-add
    @RequestMapping("/role-add")
    public Result insertRole(@RequestBody Role role, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();
        role.setCreateBy(createBy);
        Result result = roleService.saveRole(role);
        return result;

    }

    //根据角色id修改角色状态的方法
    @RequestMapping("/role-state-update")
    public Result updateRoleState(@RequestBody Role role){
        Result result = roleService.setRoleState(role);
        return result;
    }

    //  根据角色id删除角色  --  /role/role-delete/{roleId}
    @RequestMapping("/role-delete/{roleId}")
    public Result deleteRole(@PathVariable Integer roleId){//@PathVariable将路径占位符roleId的值赋值给请求参数的入参roleId
        Result result = roleService.deleteRoleById(roleId);
        return result;
    }


   //查询角色分配到所有权限菜单的业务
    @RequestMapping("/role-auth")
    public Result roleAuth(Integer roleId){
        List<Integer> authIdList = roleService.queryRoleAuthIds(roleId);
        return Result.ok(authIdList);
    }

    //给角色分配菜单权限
    @RequestMapping("/auth-grant")
    public Result grantAuth(@RequestBody AssignAuthDto assignAuthDto){
        roleService.saveRoleAuth(assignAuthDto);
        return Result.ok("权限分配成功！");
    }


    //修改角色的url接口/role/role-update
    @RequestMapping("/role-update")
    public Result updateRole(@RequestBody Role role,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){

        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int updateBy = currentUser.getUserId();
        role.setUpdateBy(updateBy);
        Result result = roleService.setRoleByRid(role);
        return result;

    }

}
