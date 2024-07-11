package com.pn.controller;


import com.pn.dto.AssignRoleDto;
import com.pn.entity.CurrentUser;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.entity.User;
import com.pn.page.Page;
import com.pn.service.RoleService;
import com.pn.service.UserService;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user-list")
    public Result userList(Page page,User user){
        Page page1 = userService.queryUserByPage(page,user);
        return Result.ok(page1);
    }


    @Autowired
    private TokenUtils tokenUtils;
    @RequestMapping("/addUser")
    public Result addUser(@RequestBody User user, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createId = currentUser.getUserId();
        user.setCreateBy(createId);
        Result result = userService.saveUser(user);
        return result;
    }

    @RequestMapping("/updateState")
    public Result updateUserState(@RequestBody User user){
        Result result = userService.setUserState(user);
        return result;
    }



    @Autowired
    private RoleService roleService;
    //这个请求是来回显用户分配到的哪些角色的  --  /user/user-role-list/37
    //最终封装的结果是该用户对应的角色List -- List<Role>
    @RequestMapping("/user-role-list/{userId}")
    public Result userRoleList(@PathVariable Integer userId){//@PathVariable可以指定值的，如果路径占位符的名字和参数的名字是一样的可以不写，自动就可以注入值
        List<Role> roleList = roleService.queryUserRoleByUid(userId);
        return Result.ok(roleList);
    }


    //给用户分配角色 -- /user/assignRole
    @RequestMapping("/assignRole")
    public Result assignRole(@RequestBody AssignRoleDto assignRoleDto){
        userService.assignRole(assignRoleDto);
        return Result.ok("分配角色成功！");
    }

    //删除用户  --  /user/deleteUser/37
    @RequestMapping("/deleteUser/{userId}")
    public Result deleteUserById(@PathVariable Integer userId){
        //Arrays工具类的asList方法返回一个List集合，而且最终List集合的泛型和传入的参数的类型是一致的List<Integer>，你传入的每一个参数最终都会对应List集合的一个元素
        Result result = userService.removeUserByIds(Arrays.asList(userId));
        return result;
    }

    //批量删除用户 -- /user/deleteUserList
    @RequestMapping("/deleteUserList")
    public Result deleteUserByIds(@RequestBody List<Integer> userIdList){
        Result result = userService.removeUserByIds(userIdList);
        return result;
    }

    //修改用户 -- /user/updateUser
    @RequestMapping("/updateUser")
    public Result updateUser(@RequestBody User user,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int updatePeopleId = currentUser.getUserId();
        user.setUpdateBy(updatePeopleId);
        Result result = userService.setUserById(user);
        return result;
    }

    @RequestMapping("/updatePwd/{userId}")
    public Result updatePwd(@PathVariable Integer userId){
        Result result = userService.setPwdById(userId);
        return result;
    }

}
