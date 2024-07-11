package com.pn.controller;

import com.pn.entity.InStore;
import com.pn.entity.Result;
import com.pn.entity.Store;
import com.pn.page.Page;
import com.pn.service.InStoreService;
import com.pn.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/instore")
@RestController
public class InStoreController {
    @Autowired
    private InStoreService inStoreService;


    @Autowired
    private StoreService storeService;

    //查询所有仓库的url接口  --  /instore/store-list
    @RequestMapping("/store-list")
    public Result storeList(){
        List<Store> storesList = storeService.queryAllStore();
        return Result.ok(storesList);
    }

    //查询所有入库单并分页 或者 根据仓库id、商品名称、起止时间查询入库单并分页  --  /instore/instore-page-list
    @RequestMapping("/instore-page-list")
    public Result inStoreListPage(InStore inStore, Page page){
        page = inStoreService.queryInStorePage(inStore, page);
        return Result.ok(page);
    }


    //确认入库的url接口 /instore/instore-confirm
    @RequestMapping("/instore-confirm")
    public Result confirmInStore(@RequestBody InStore inStore){
        Result result = inStoreService.inStoreConfirm(inStore);
        return result;
    }
}
