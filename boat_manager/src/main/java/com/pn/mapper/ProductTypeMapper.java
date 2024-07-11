package com.pn.mapper;

import com.pn.entity.ProductType;

import java.util.List;

public interface ProductTypeMapper {

    //查询所有商品的类别
    public List<ProductType> findAllProductType();

    //根据编号或名称查询商品分类
    public ProductType findTypeByCodeOrName(ProductType productType);

    //添加商品分类
    public int insertProductType(ProductType productType);

    //根据类别的id或父级的id删除商品分类
    int removeProductType(Integer typeId);

    //根据分类id修改分类信息
    int serProductTypeById(ProductType productType);
}
