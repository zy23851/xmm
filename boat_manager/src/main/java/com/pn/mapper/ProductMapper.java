package com.pn.mapper;

import com.pn.entity.Product;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    //查询商品行数的方法
    Integer findProductRowCount(Product product);

    //分页查询商品的方法
    List<Product> findProductPage(@Param("page") Page page, @Param("product") Product product);


    //根据商品型号查询商品
    Product findProductByNum(String productNum);
    //添加商品的方法
    int insertProduct(Product product);

    //根据商品id修改商品的上下架状态
    int setStateByPid(Integer productId,String upDownState);

    //根据商品id删除单个或多个商品
    int removeProductByIds(List<Integer> productIdList);

    //根据商品id修改商品的方法
    int setProductById(Product product);

    //根据商品id修改库存
    int setInventById(Integer productId, Integer invent);

    //根据商品id查询商品库存
    int findInventById(Integer productId);
}
