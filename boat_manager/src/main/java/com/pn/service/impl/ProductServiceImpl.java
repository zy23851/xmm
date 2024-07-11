package com.pn.service.impl;

import com.pn.entity.Product;
import com.pn.entity.Result;
import com.pn.mapper.ProductMapper;
import com.pn.page.Page;
import com.pn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    //分页查询商品的业务方法
    @Override
    public Page queryProductPage(Page page, Product product) {
        Integer count = productMapper.findProductRowCount(product);
        List<Product> productList = productMapper.findProductPage(page, product);
        page.setResultList(productList);
        page.setTotalNum(count);
        return page;
    }

    //添加商品的业务方法
    @Value("${file.access-path}")
    private String imgAccessPath;

    @Override
    public Result saveProduct(Product product) {
        //判断商品型号是否存在
        Product prod = productMapper.findProductByNum(product.getProductNum());
        if(prod != null){
            return Result.err(Result.CODE_ERR_BUSINESS,"该型号的商品已经存在！");
        }

        //保存到数据库的图片要加上前缀,我也不太清楚为什么要这样*************************

        String imgPath = imgAccessPath+product.getImgs();
        product.setImgs(imgPath);

        int i = productMapper.insertProduct(product);
        if(i>0){
            return Result.ok("商品添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"商品添加失败！");
    }

    //根据商品id修改商品的上下架状态
    @Override
    public Result updateStateByPid(Product product) {
        int i = productMapper.setStateByPid(product.getProductId(), product.getUpDownState());
        if(i>0){
            return Result.ok("商品上下架修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"商品上下架修改失败！");
    }



    //根据商品id删除单个或多个商品
    @Override
    public Result deleteProductByIds(List<Integer> productIdList) {
        int i = productMapper.removeProductByIds(productIdList);
        if(i>0){
            return Result.ok("删除商品成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"删除商品失败！");
    }

    //根据商品id修改商品的方法
    @Override
    public Result setProductByPid(Product product) {
        //判断上传的图片是否修改
        String imgs = product.getImgs();
        if(!imgs.contains(imgAccessPath)){//表示修改了图片
            imgs = imgAccessPath + imgs;
        }
        product.setImgs(imgs);
        //对型号的修改
        Product prod = productMapper.findProductByNum(product.getProductNum());
        if(prod!=null){//说明商品型号已经存在
            if(product.getProductId() == prod.getProductId()){//表示没有修改商品型号
                int i = productMapper.setProductById(product);
                if(i>0){
                    return Result.ok("修改商品成功！");
                }
                return Result.err(Result.CODE_ERR_BUSINESS,"修改商品失败！");
            }else{//表示你修改的商品型号重复了
                return Result.err(Result.CODE_ERR_BUSINESS,"商品型号已经存在！");
            }
        }
        //商品型号不存在
        int i = productMapper.setProductById(product);
        if(i>0){
            return Result.ok("修改商品成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"修改商品失败！");
    }


}
