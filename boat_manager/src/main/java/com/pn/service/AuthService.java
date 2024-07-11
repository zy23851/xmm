package com.pn.service;

import com.pn.entity.Auth;

import java.util.List;

public interface AuthService {

    //查询用户菜单树的业务方法 -- 返回值的就是   菜单树了  不再是那个查出来的了
    public List<Auth> authTreeByUid(Integer userId);

    //查询所有权限菜单树的方法,返回值已经是菜单树了
    List<Auth> allAuthTree();
}
