package com.pn.service;

import com.pn.entity.Product;
import com.pn.entity.Result;
import com.pn.page.Page;

import java.util.List;

public interface ProductService {
    //分页查询商品的业务方法
    Page queryProductPage(Page page, Product product);

    //添加商品的业务方法
    Result saveProduct(Product product);

    //根据商品id修改商品的上下架状态
    Result updateStateByPid(Product product);

    //根据商品id删除单个或多个商品
    Result deleteProductByIds(List<Integer> productIdList);
    //根据商品id修改商品的方法
    Result setProductByPid(Product product);
}
