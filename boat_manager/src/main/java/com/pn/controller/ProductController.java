package com.pn.controller;

import com.pn.entity.*;
import com.pn.page.Page;
import com.pn.service.*;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/product")

public class ProductController {

    @Autowired
    private StoreService storeService;

    //  -- 查询所有仓库的 product/store-list
    @RequestMapping("/store-list")
    public Result storeList(){
        List<Store> storeList = storeService.queryAllStore();
        return Result.ok(storeList);
    }




    @Autowired
    private BrandService brandService;
    //查询所有品牌/product/brand-list  给搜索商品皮牌下拉框组装数据的
    @RequestMapping("/brand-list")
    public Result brandList(){
        List<Brand> brandList = brandService.queryAllBrand();
        brandList.forEach(brand -> System.out.println(brand));
        return Result.ok(brandList);
    }




    //分页查询商品的url接口 /product/product-page-list

    @Autowired
    private ProductService productService;
    @RequestMapping("/product-page-list")
    public Result productListPage(Page page, Product product){
        page = productService.queryProductPage(page, product);
        return Result.ok(page);
    }


    //查询所有供应商的方法
    @Autowired
    private SupplyService supplyService;
    @RequestMapping("/supply-list")
    public Result supplyList(){
        List<Supply> supplies = supplyService.queryAllSupply();
        return Result.ok(supplies);
    }


    //查询所有商品分类树的方法
    @Autowired
    private ProductTypeService productTypeService;
    @RequestMapping("/category-tree")
    public Result loadTypeTree(){
        List<ProductType> typeTreeList = productTypeService.productTypeTree();
        return Result.ok(typeTreeList);

    }

    @Autowired
    private PlaceService placeService;
    //查询所有产地的方法
    @RequestMapping("/place-list")
    public Result placeList(){
        List<Place> placeList = placeService.queryAllPlace();
        return Result.ok(placeList);
    }

    @Autowired
    private UnitService unitService;
    //查询所有单位的方法
    @RequestMapping("/unit-list")
    public Result unitList(){
        List<Unit> unitList = unitService.queryAllUnit();
        return Result.ok(unitList);
    }


/*
    /product/img-upload -- 上传图片
    file:上传的图片的字节数据
*/

    @Value("${file.upload-path}")
    private String imgPath;

    @CrossOrigin//表示url接口允许跨域请求
    @RequestMapping("/img-upload")
    public Result uploadImg(MultipartFile file){

        try {
            //拿到封装了类路径的file对象，这个路径是类路径是带了classpath的，不能直接new File对象，必须借助工具类ResourceUtil
            File resourceFile = ResourceUtils.getFile(imgPath);
            //static/img/upload
            //拿到该类路径对应的磁盘目录 -- 不管部署到哪个服务器，都能获取到这个类路径目录对应的磁盘路径目录
            String absolutePath = resourceFile.getAbsolutePath();

            //获得文件名（图片的名称  --  xxx.jpg）
            String fileName = file.getOriginalFilename();

            //得到完整的上传图片的全路径（完整的磁盘文件（xxx.jpg）所在位置）
            String filePath = absolutePath+"\\"+ fileName;

            //上传文件
            file.transferTo(new File(filePath));

            return Result.ok("图片上传成功！");

        } catch (Exception e) {
            return Result.err(Result.CODE_ERR_BUSINESS,"图片上传失败！");
        }
    }

    /*
         /product/product-add -- 添加商品
     */
    @Autowired
    private TokenUtils tokenUtils;
    @RequestMapping("/product-add")
    public Result addProduct(@RequestBody Product product,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();
        product.setCreateBy(createBy);

        Result result = productService.saveProduct(product);
        return result;
    }


    /*
        /product/state-change
     */
    @RequestMapping("/state-change")
    public Result changeStateByPid(@RequestBody Product product){
        Result result = productService.updateStateByPid(product);
        return result;
    }

    /*
         删除单个商品
        /product/product-delete/31 --> /product/product-delete/{productId}
     */
    @RequestMapping("/product-delete/{productId}")
    public Result deleteProduct(@PathVariable("productId") Integer id){//两个名字写的一样，则注解的属性不用配置，便可以注入值。如果写的不一样，注解的属性指明路径中的名称，表示将路径中的名称赋值给形参
        /*
        List<Integer> productIdList = new ArrayList<>();
        productIdList.add(id);
        Result result = productService.deleteProductByIds(productIdList);
        */
        Result result = productService.deleteProductByIds(Arrays.asList(id));/*将id封装到list*/
        return result;
    }

    /*
        删除多个商品
        /product/product-list-delete
     */
    @RequestMapping("/product-list-delete")
    public Result deleteProductList(@RequestBody List<Integer> productIdList){
        Result result = productService.deleteProductByIds(productIdList);
        return result;
    }


    //修改商品  --  /product/product-update
    @RequestMapping("/product-update")
    public Result updateProduct(@RequestBody Product product,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){//将json数据的属性的值赋给形参对象对应的属性
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int updateBy = currentUser.getUserId();
        product.setUpdateBy(updateBy);
        Result result = productService.setProductByPid(product);
        return result;
    }
}
