package com.pn.service.impl;

import com.pn.entity.Supply;
import com.pn.mapper.SupplyMapper;
import com.pn.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "com.pn.service.impl.SupplyServiceImpl")
public class SupplyServiceImpl implements SupplyService {

    @Autowired
    private SupplyMapper supplyMapper;


    @Override
    @Cacheable(key = "'all:supply'")
    public List<Supply> queryAllSupply() {
        return supplyMapper.findAllSupply();
    }
}
