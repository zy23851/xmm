package com.pn.service.impl;

import com.pn.entity.Purchase;
import com.pn.entity.Result;
import com.pn.mapper.PurchaseMapper;
import com.pn.page.Page;
import com.pn.service.PurchaseService;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;
    //添加采购单的业务方法
    @Override
    public Result savePurchase(Purchase purchase) {
        //purchase.setFactBuyNum(purchase.getBuyNum());
        int i = purchaseMapper.insertPurchase(purchase);
        if(i>0){
            return Result.ok("采购单添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"采购单添加失败！");
    }

    //获得采购单列表
    @Override
    public Page queryPurchasePage(Purchase purchase,Page page) {
        List<Purchase> purchaseList = purchaseMapper.findPurchasePage(purchase, page);
        page.setResultList(purchaseList);
        page.setTotalNum(purchaseMapper.findPurchaseRowCount(purchase));
        return page;
    }

    //根据id删除采购单
    @Override
    public Result deletePurchaseById(Integer buyId) {
        int i = purchaseMapper.removePurchaseById(buyId);
        if(i>0){
            return Result.ok("采购单删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"采购单删除失败！");
    }

    //根据id修改采购单的预计购买数量和实际购买数量
    @Override
    public Result updatePurchaseById(Purchase purchase) {
        int i = purchaseMapper.setNumById(purchase);
        if(i>0){
            return Result.ok("采购单修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"采购单修改失败！");
    }
}
