package com.pn.controller;

import com.pn.entity.Auth;
import com.pn.entity.Result;
import com.pn.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    //查询所有权限菜单树的url接口
    @RequestMapping("/auth-tree")
    public Result loadAllAuthTree(){
        List<Auth> authTree = authService.allAuthTree();
        return Result.ok(authTree);
    }

}
