package com.pn.service.impl;

import com.pn.entity.Place;
import com.pn.mapper.PlaceMapper;
import com.pn.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.pn.service.impl.PlaceServiceImpl")
@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private PlaceMapper placeMapper;

    //查询所有产地的方法
    @Cacheable(key = "'all:place'")
    @Override
    public List<Place> queryAllPlace() {
        List<Place> allPlaceList = placeMapper.findAllPlace();
        return allPlaceList;
    }
}
