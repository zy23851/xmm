package com.pn.service;

import com.pn.entity.Store;
import com.pn.vo.StoreCountVo;

import java.util.List;

public interface StoreService {

    //查询所有仓库的业务方法
    List<Store> queryAllStore();
    //查询每个仓库商品数量的业务方法
    List<StoreCountVo> queryStoreCount();
}
