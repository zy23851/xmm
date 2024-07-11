package com.pn.controller;

import com.pn.entity.*;
import com.pn.page.Page;
import com.pn.service.InStoreService;
import com.pn.service.PurchaseService;
import com.pn.service.StoreService;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/purchase")
@RestController
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    //  添加采购单的方法/purchase/purchase-add
    @RequestMapping("/purchase-add")
    public Result addPurchase(@RequestBody Purchase purchase){
        Result result = purchaseService.savePurchase(purchase);
        return result;
    }

    @Autowired
    private StoreService storeService;
    //查询所有仓库 -- /purchase/store-list
    @RequestMapping("/store-list")
    public Result storeList(){
        List<Store> storeList = storeService.queryAllStore();
        return Result.ok(storeList);
    }

    //查询所有采购单并分页 或者是 根据仓库id、起止时间、商品名称、采购员、是否入库查询采购单并分页; -- /purchase/purchase-page-list
    @RequestMapping("/purchase-page-list")
    public Result purchaseListPage(Purchase purchase, Page page){//是能自动匹配自动赋值的
        page = purchaseService.queryPurchasePage(purchase, page);
        return Result.ok(page);
    }

    //删除采购单 -- /purchase/purchase-delete/{buyId} -- 根据id删除采购单
    @RequestMapping("/purchase-delete/{buyId}")
    public Result deletePurchase(@PathVariable Integer buyId){
        Result result = purchaseService.deletePurchaseById(buyId);
        return result;
    }

    //修改采购单  --  /purchase/purchase-update
    @RequestMapping("/purchase-update")
    public Result updatePurchase(@RequestBody Purchase purchase){
        Result result = purchaseService.updatePurchaseById(purchase);
        return result;
    }


    @Autowired
    private InStoreService inStoreService;
    @Autowired
    private TokenUtils tokenUtils;
    //生成入库单   /purchase/in-warehouse-record-add
    @RequestMapping("/in-warehouse-record-add")
    public Result addInstore(@RequestBody Purchase purchase ,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();
        InStore inStore = new InStore();
        inStore.setCreateBy(createBy);
        inStore.setProductId(purchase.getProductId());
        inStore.setStoreId(purchase.getStoreId());
        inStore.setInNum(purchase.getFactBuyNum());
        Result result = inStoreService.saveInStore(inStore, purchase.getBuyId());
        return result;

    }
}
