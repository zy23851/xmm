package com.pn.service.impl;

import com.pn.entity.Store;
import com.pn.mapper.StoreMapper;
import com.pn.service.StoreService;
import com.pn.vo.StoreCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.pn.service.impl.StoreServiceImpl")
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Cacheable(key = "'all:store'")
    @Override
    public List<Store> queryAllStore() {
        return storeMapper.findAllStore();
    }

    //查询每个仓库商品数量的业务方法
    @Override
    public List<StoreCountVo> queryStoreCount() {
        List<StoreCountVo> storeCountVoList = storeMapper.findStoreCount();
        return storeCountVoList;
    }
}
