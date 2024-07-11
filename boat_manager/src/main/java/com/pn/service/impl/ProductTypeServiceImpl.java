package com.pn.service.impl;

import com.pn.entity.ProductType;
import com.pn.entity.Result;
import com.pn.mapper.ProductTypeMapper;
import com.pn.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@CacheConfig(cacheNames = "com.pn.service.impl.ProductTypeServiceImpl")
@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    //获得商品分类树
    @Cacheable(key = "'all:typeTree'")
    @Override
    public List<ProductType> productTypeTree() {
        //先查出来所有的商品的类别
        List<ProductType> allProductTypeList = productTypeMapper.findAllProductType();
        //转成商品分类树并返回
        List<ProductType> typeTreeList = allTypeToTree(allProductTypeList, 0);
        return typeTreeList;
    }



    //递归算法转成分类树
    private List<ProductType> allTypeToTree(List<ProductType> allProductTypeList,Integer pid){

        List<ProductType> firstLevelProductTypeList = new ArrayList<>();
        for (ProductType productType : allProductTypeList) {
            if(productType.getParentId() == pid){
                firstLevelProductTypeList.add(productType);
            }
        }

        for (ProductType productType : firstLevelProductTypeList) {
            List<ProductType> secondLevelProductTypeList = allTypeToTree(allProductTypeList, productType.getTypeId());
            productType.setChildProductCategory(secondLevelProductTypeList);
        }
        return firstLevelProductTypeList;
    }

    //判断分类编码是否存在的业务方法
    @Override
    public Result checkTypeCode(String typeCode) {
        ProductType productType = new ProductType();
        productType.setTypeCode(typeCode);
        ProductType prodType = productTypeMapper.findTypeByCodeOrName(productType);
        return Result.ok(prodType == null);
    }

    //添加商品分类
    @CacheEvict(key = "'all:typeTree'")//清理缓存
    @Override
    public Result saveProductType(ProductType productType) {

        //校验分类名称是否存在
        ProductType productType1 = new ProductType();
        productType1.setTypeName(productType.getTypeName());

        ProductType prodType = productTypeMapper.findTypeByCodeOrName(productType1);
        if(prodType != null){//说明商品分类名已经存在
            return Result.err(Result.CODE_ERR_BUSINESS,"商品分类名称已经存在！");
        }
        int i = productTypeMapper.insertProductType(productType);
        if(i>0){
            return Result.ok("添加商品分类成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"添加商品分类失败！");
    }

    //根据类别的id或父级的id删除商品分类
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result deleteProductType(Integer typeId) {

        int i = productTypeMapper.removeProductType(typeId);
        if(i>0){
            return Result.ok("商品分类删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"商品分类删除失败！");
    }


    //根据分类id修改分类信息
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result setProductType(ProductType productType) {
        //校验分类名称是否存在
        ProductType productType1 = new ProductType();
        productType1.setTypeName(productType.getTypeName());

        ProductType prodType = productTypeMapper.findTypeByCodeOrName(productType1);
        //查到自己的或别人的 ---> prodType != null
        //!prodType.getTypeId().equals(productType.getTypeId()) --> 如果不成立，说明是自己的id和自己的id相等，就不用管，说明当初填写表单的时候，你没有该商品名称，用的还是之前的；如果成立，说明用到了别人的已经存在的分类名称了
        if(prodType != null && !prodType.getTypeId().equals(productType.getTypeId())){//说明商品分类名已经存在,并且
            return Result.err(Result.CODE_ERR_BUSINESS,"商品分类名称已经存在！");
        }

        //查不到，直接用
        int i = productTypeMapper.serProductTypeById(productType);
        if (i>0){
            return Result.ok("修改商品类别成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"修改商品类别失败！");
    }

}
