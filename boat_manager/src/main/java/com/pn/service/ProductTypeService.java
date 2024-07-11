package com.pn.service;

import com.pn.entity.ProductType;
import com.pn.entity.Result;

import java.util.List;

public interface ProductTypeService {

    //获得商品分类树
    List<ProductType> productTypeTree();

    //判断分类编码是否存在的业务方法
    Result checkTypeCode(String typeCode);

    //添加商品分类的业务方法
    Result saveProductType(ProductType productType);

    //根据类别的id或父级的id删除商品分类
    Result deleteProductType(Integer typeId);

    //根据分类id修改分类信息
    Result setProductType(ProductType productType);
}
