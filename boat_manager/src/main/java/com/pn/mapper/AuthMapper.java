package com.pn.mapper;

import com.pn.entity.Auth;

import java.util.List;

public interface AuthMapper {

    //根据userId查询用户权限下的所有菜单的方法
    List<Auth> findAuthByUid(Integer userId);

    //查询所有权限菜单的方法
    List<Auth> findAllAuth();


}
