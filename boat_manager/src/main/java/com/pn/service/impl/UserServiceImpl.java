package com.pn.service.impl;

import com.pn.dto.AssignRoleDto;
import com.pn.entity.Result;
import com.pn.entity.User;
import com.pn.entity.UserRole;
import com.pn.mapper.RoleMapper;
import com.pn.mapper.UserMapper;
import com.pn.mapper.UserRoleMapper;
import com.pn.page.Page;
import com.pn.service.UserService;
import com.pn.utils.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

//将bean对象创建加载到容器中
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    //这里报错其实不是真正的错误，因为我们是在启动类上加的mapper扫描，
    // em....可能是要启动的时候，才会扫描mapper接口，创建它的代理类对象并放入容器中，然后这里才能按照类型注入
    //并不影响程序运行
    private UserMapper userMapper;

    @Override
    public User queryUserByCode(String userCode) {
        return userMapper.findUserByCode(userCode);
    }

    @Override
    public Page queryUserByPage(Page page, User user) {

        Integer userRowCount = userMapper.findUserRowCount(user);

        List<User> userList = userMapper.findUserByPage(page, user);

        page.setTotalNum(userRowCount);

        page.setResultList(userList);

        return page;
    }


    @Override
    public Result saveUser(User user) {

        User u = userMapper.findUserByCode(user.getUserCode());
        if(u != null){//账号已存在
            return Result.err(Result.CODE_ERR_BUSINESS,"账号已存在，添加失败");
        }

        String userPwd = user.getUserPwd();
        String s = DigestUtil.hmacSign(userPwd);
        user.setUserPwd(s);
        int i = userMapper.insertUser(user);
        if(i > 0){
            return Result.ok("用户添加成功");
        }

        return Result.err(Result.CODE_ERR_BUSINESS,"用户添加失败");
    }

    @Override
    public Result setUserState(User user) {
        int i = userMapper.serStateByUid(user.getUserId(), user.getUserState());
        if(i > 0){
            return Result.ok("启用或禁用用户状态成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"启用或禁用用户状态失败！");
    }

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    //给用户分配角色的业务方法
    /*
        这个方法中执行了多个DML语句，应该开始事务
     */
    @Transactional
    @Override
    public void assignRole(AssignRoleDto assignRoleDto) {

        userRoleMapper.removeUserRoleByUid(assignRoleDto.getUserId());
        List<String> roleNameList = assignRoleDto.getRoleCheckList();
        for (String roleName : roleNameList) {
                Integer roleId = roleMapper.findRoleIdByName(roleName);
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(assignRoleDto.getUserId());
                userRoleMapper.insertUserRole(userRole);
            }
    }

    //删除用户的业务
    @Override
    public Result removeUserByIds(List<Integer> userIdList) {
        int i = userMapper.setIsDeleteByUids(userIdList);
        if(i>0){
            return Result.ok("用户删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"用户删除失败！");
    }

    //修改用户信息
    @Override
    public Result setUserById(User user) {
        int i = userMapper.setUserNameByUid(user);
        if (i > 0){
            return Result.ok("用户修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"用户修改失败！");
    }

    //根据用户id重置密码
    @Override
    public Result setPwdById(Integer userId) {

        String userPwd = DigestUtil.hmacSign("123456");
        int i = userMapper.setPwdByUid(userId,userPwd);
        if (i>0){
            return Result.ok("重置密码成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"重置密码失败！");
    }


}
