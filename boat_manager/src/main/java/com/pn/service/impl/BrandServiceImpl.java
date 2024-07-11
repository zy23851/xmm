package com.pn.service.impl;

import com.pn.entity.Brand;
import com.pn.mapper.BrandMapper;
import com.pn.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "com.pn.service.impl.BrandServiceImpl")
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    //查询所有品牌的业务方法
    //@Cacheable(key = "'all:brand'")
    @Override
    public List<Brand> queryAllBrand() {
        List<Brand> brandList = brandMapper.findAllBrand();
        brandList.forEach(brand -> System.out.println(brand));
        return brandList;
    }



}
