package com.pn.controller;

import com.pn.entity.ProductType;
import com.pn.entity.Result;
import com.pn.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productCategory")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;
    //查询商品分类树 -- /productCategory/product-category-tree
    @RequestMapping("/product-category-tree")
    public Result productCategoryTree(){
        List<ProductType> productTypesTree = productTypeService.productTypeTree();
        return Result.ok(productTypesTree);
    }

    //判断分类编码是否存在的业务方法 -- /productCategory/verify-type-code?typeCode=lingsh
    @RequestMapping("/verify-type-code")
    public Result checkTypeCode(String typeCode){
        Result result = productTypeService.checkTypeCode(typeCode);
        return result;
    }

    //添加商品类别信息 /productCategory/type-add
    @RequestMapping("/type-add")
    public Result addProductType(@RequestBody ProductType productType){
        Result result = productTypeService.saveProductType(productType);
        return result;
    }


    //删除商品类别信息 -- /productCategory/type-delete/{typeId}
    @RequestMapping("/type-delete/{typeId}")
    public Result deleteType(@PathVariable Integer typeId){
        Result result = productTypeService.deleteProductType(typeId);
        return result;
    }

    //根据商品id修改商品类别信息 -- /productCategory/type-update
    @RequestMapping("/type-update")
    public Result updateProductType(@RequestBody ProductType productType){
        Result result = productTypeService.setProductType(productType);
        return result;
    }
}
