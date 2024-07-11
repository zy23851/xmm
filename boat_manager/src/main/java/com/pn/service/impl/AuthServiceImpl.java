package com.pn.service.impl;

import com.alibaba.fastjson.JSON;
import com.pn.entity.Auth;
import com.pn.mapper.AuthMapper;
import com.pn.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@CacheConfig(cacheNames = "com.pn.service.impl.AuthServiceImpl")
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;



    /*
        redis缓存中，key存authTree:userId，value存的是菜单树List<Auth>转成的json字符串
     */
    @Override
    public List<Auth> authTreeByUid(Integer userId) {
        //第一步先从缓存中取，看有没有
        String authTreeJson = redisTemplate.opsForValue().get("authTree:" + userId);
        if(StringUtils.hasText(authTreeJson)){
            //如果有，将json串转回菜单树，再返回
            List<Auth> authTreeList = JSON.parseArray(authTreeJson, Auth.class);//菜单树
            return authTreeList;
        }

        //如果redis缓存中没有找到
        List<Auth> allAuthList = authMapper.findAuthByUid(userId);//用户权限下的所有菜单，也就是list集合中封装的是所有菜单对象，没有所谓的一级菜单，二级菜单这些
        //将查到的用户权限下的所有菜单转成菜单树 -- 递归算法 -- 记住 -- 常用的
        List<Auth> authTreeList = allAuthToAuthTree(allAuthList, 0);
        //将菜单树放入redis缓存
        redisTemplate.opsForValue().set("authTree:"+userId,JSON.toJSONString(authTreeList));
        //返回菜单树
        return authTreeList;
    }



    //将查出来的用户权限下的所有菜单List<Auth>转成菜单树List<Auth>的递归算法
    private  List<Auth> allAuthToAuthTree(List<Auth> allAuthList,Integer pid){

        List<Auth> firstLevelAuthList = new ArrayList<>();
        for (Auth auth : allAuthList) {
            /*
                追加的List<Auth>集合属性 -- 用于存储当前权限(菜单)的子级权限(菜单)
	            private List<Auth> childAuth;
             */
            if (auth.getParentId() == pid){
                firstLevelAuthList.add(auth);
            }
        }

        for (Auth auth : firstLevelAuthList) {
            List<Auth> childAuthList = allAuthToAuthTree(allAuthList, auth.getAuthId());
            auth.setChildAuth(childAuthList);
        }

        return firstLevelAuthList;
    }


    //查询所有权限菜单树的方法,返回值已经是菜单树了
    @Cacheable(key = "'all:authTree'")
    @Override
    public List<Auth> allAuthTree() {
        /**
         * 先走缓存，第一次肯定没有，查出来，菜单树放到缓存里，并返回显示，下次直接从缓存拿
         */
        List<Auth> allAuthList = authMapper.findAllAuth();
        List<Auth> authTreeList = allAuthToAuthTree(allAuthList, 0);
        return authTreeList;
    }
}
