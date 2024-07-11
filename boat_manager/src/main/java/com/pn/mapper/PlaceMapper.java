package com.pn.mapper;

import com.pn.entity.Place;

import java.util.List;

public interface PlaceMapper {

    //查询所有产地的方法
    List<Place> findAllPlace();
}
