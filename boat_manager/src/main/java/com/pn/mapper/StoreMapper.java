package com.pn.mapper;

import com.pn.entity.Store;
import com.pn.vo.StoreCountVo;

import java.util.List;

public interface StoreMapper {

    //查讯所有仓库的方法
    List<Store> findAllStore();

    //查询每个仓库的商品数量的方法
    List<StoreCountVo> findStoreCount();
}
