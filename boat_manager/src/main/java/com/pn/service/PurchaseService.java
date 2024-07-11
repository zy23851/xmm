package com.pn.service;

import com.pn.entity.Purchase;
import com.pn.entity.Result;
import com.pn.page.Page;


public interface PurchaseService {
    //添加采购单的业务方法
    Result savePurchase(Purchase purchase);
    //获得采购单列表
    Page queryPurchasePage(Purchase purchase,Page page);

    //根据id删除采购单
    Result deletePurchaseById(Integer buyId);
    //根据id修改采购单的预计购买数量和实际购买数量
    Result updatePurchaseById(Purchase purchase);
}
