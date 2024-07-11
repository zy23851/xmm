package com.pn.mapper;

import com.pn.entity.Purchase;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseMapper {
    //添加采购单的方法
    int insertPurchase(Purchase purchase);

    //获得采购单总行数 或者是 带条件的总行数
    Integer findPurchaseRowCount(Purchase purchase);
    //查询所有采购单并分页 或者是 根据仓库id、起止时间、商品名称、采购员、是否入库查询采购单并分页
    List<Purchase> findPurchasePage(@Param("purchase") Purchase purchase, @Param("page") Page page);

    //根据id删除采购单
    int removePurchaseById(Integer buyId);
    //根据id修改采购单的预计购买数量和实际购买数量
    int setNumById(Purchase purchase);
    //根据id修改采购单的入库状态
    int setIsInById(Integer buyId);
}
