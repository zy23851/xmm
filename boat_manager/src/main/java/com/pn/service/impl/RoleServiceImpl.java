package com.pn.service.impl;

import com.pn.dto.AssignAuthDto;
import com.pn.entity.Auth;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.entity.RoleAuth;
import com.pn.mapper.AuthMapper;
import com.pn.mapper.RoleAuthMapper;
import com.pn.mapper.RoleMapper;
import com.pn.page.Page;
import com.pn.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//指定缓存的名称（数据保存到redis中的键的前缀，一般值是标注该注解的类的全限定类名）
@CacheConfig(cacheNames = "com.pn.service.impl.RoleServiceImpl")
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    //查询所有角色信息,当数据量很大，并且数据不会轻易发生变化的，可以考虑放到缓存redis中
    //使用注解方式开启redis缓存分三步
    /*
        1.在启动类上标志注解@EnableCaching，表示允许使用注解开发，去使用redis缓存
        2.在查询方法所在的类的上面标志注解@@CacheConfig(cacheNames = "标注该注解的类的全限定类名")
        3.在查询方法上标注注解@Cacheable(key = "'指定缓存的键'") -- 一般还有个''  记住就好

        4.清理缓存@CacheEvict(key = "'键'")

     */
    @Cacheable(key = "'all:role'")
    @Override
    public List<Role> queryAllRole() {
        return roleMapper.findAllRole();
    }


    //查询指定用户的角色信息
    @Override
    public List<Role> queryUserRoleByUid(Integer userId) {
        return roleMapper.findUserRoleByUid(userId);
    }








    //分页查询角色的业务方法
    @Override
        public Page queryRolePage(Role role, Page page) {
        Integer roleRowCount = roleMapper.findRoleRowCount(role);
        List<Role> rolePageList = roleMapper.findRolePage(role, page);
        page.setTotalNum(roleRowCount);
        page.setResultList(rolePageList);
        return page;
    }

    /**
     * 对清空缓存的思路进行讲解：
     *   1、我们假设当用户点击用户列表时，发送的/role-list请求是作用于两个地方的，一个在下拉列表显示，
     *   一个在分配角色中进行显示，显示的都是角色状态可用的角色
     *   当用户第一次点击用户列表，发送请求，执行sql语句之后，将数据List<>响应给前端，并且存储到redis中，这样只要角色的状态，个数等
     *   不发生变化，我们无论点多少次用户列表，都是从redis中拿数据，不查mysql数据库，提高效率
     *   当角色状态或个数等改变时，如果我们点击用户列表，此时依然走缓存，但其实缓存的数据已经不再适用了，它没有与时俱进，还是
     *   老一份数据，这样角色的更改就不能够得到显示
     *   因此当角色的个数，角色的状态发生改变的时候，应该清空缓存。这样当角色个数，状态等改变时，我们重新点击用户列表，首先会重新进行
     *   mysql数据库查询，保证是最新的数据，同时再存到redis，也是为了在角色没有改变时，提高查询效率。
     *
     *   2、数据回显呢？
     *   数据回显的请求是/user-role-list/{userId}，我们数据回显的功能是前端将本次业务查到的List<>集合和上一个查到的List<>进行对比显示的
     *
     *
     * 当角色的个数，角色的状态发生改变的时候，应该清空缓存。
     * @param role
     * @return
     */

    //添加角色的业务方法
    @CacheEvict(key = "'all:role'")
    @Override
    public Result saveRole(Role role) {
        Role r = roleMapper.findRoleByNameOrCode(role.getRoleName(), role.getRoleCode());
        if (r != null){//存在角色
            return Result.err(Result.CODE_ERR_BUSINESS,"角色已存在，添加失败！");
        }

        int i = roleMapper.insertRole(role);

        if(i > 0){

            return Result.ok("角色添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"角色添加失败！");
    }

    //根据角色id修改角色状态的方法
    @CacheEvict(key = "'all:role'")
    @Override
    public Result setRoleState(Role role) {
        int i = roleMapper.setRoleStateByRid(role.getRoleId(), role.getRoleState());
        if(i>0){
            return Result.ok("角色启用或禁用成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"角色启用或禁用失败！");
    }

    @Autowired
    private RoleAuthMapper roleAuthMapper;

    //根据角色id删除角色
    @CacheEvict(key = "'all:role'")
    @Override
    @Transactional
    public Result deleteRoleById(Integer roleId) {

        int i = roleMapper.removeRoleById(roleId);
        if(i>0){
            roleAuthMapper.removeRoleAuthByRid(roleId);//删除角色权限关系
            return Result.ok("删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"删除失败");
    }

    //根据角色id查询角色分配的菜单权限的id
    @Override
    public List<Integer> queryRoleAuthIds(Integer roleId) {
        List<Integer> authIds = roleAuthMapper.findAuthIdsByRid(roleId);
        return authIds;
    }


    /**
     * 给用户分配权限为什么不用清空缓存？
     *      因为缓存中保存的List<>并不涉及到权限的信息，它就是单纯的角色表
     *      换句话说：你修改了角色的权限，并不影响我redis缓存中的数据

     */

    //给角色分配权限的业务
    @Override
    @Transactional//事务处理
    public void saveRoleAuth(AssignAuthDto assignAuthDto) {
        roleAuthMapper.removeRoleAuthByRid(assignAuthDto.getRoleId());//先删
        List<Integer> authIds = assignAuthDto.getAuthIds();//再加
        for (Integer authId : authIds) {
            RoleAuth roleAuth = new RoleAuth();
            roleAuth.setAuthId(authId);
            roleAuth.setRoleId(assignAuthDto.getRoleId());
            roleAuthMapper.insertRoleAuth(roleAuth);
        }
    }

    //根据角色id修改角色描述的方法
    @CacheEvict(key = "'all:role'")
    @Override
    public Result setRoleByRid(Role role) {
        int i = roleMapper.setDescByRid(role);
        if(i>0){
            return Result.ok("角色修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"角色修改失败");
    }


}
