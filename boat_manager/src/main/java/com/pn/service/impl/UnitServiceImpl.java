package com.pn.service.impl;

import com.pn.entity.Unit;
import com.pn.mapper.UnitMapper;
import com.pn.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.pn.service.impl.UnitServiceImpl")
@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitMapper unitMapper;

    //查询所有单位的方法
    @Cacheable(key = "'all:unit'")
    @Override
    public List<Unit> queryAllUnit() {
        List<Unit> allUnitList = unitMapper.findAllUnit();
        return allUnitList;
    }
}
