package com.pn.controller;

import com.pn.entity.CurrentUser;
import com.pn.entity.OutStore;
import com.pn.entity.Result;
import com.pn.entity.Store;
import com.pn.page.Page;
import com.pn.service.OutStoreService;
import com.pn.service.StoreService;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/outstore")
@RestController
public class OutStoreController {

    @Autowired
    private OutStoreService outStoreService;

    @Autowired
    private TokenUtils tokenUtils;
    //  添加出库单的方法 -- /outstore/outstore-add
    @RequestMapping("/outstore-add")
    public Result addOutStore(@RequestBody OutStore outStore ,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();
        outStore.setCreateBy(createBy);
        Result result = outStoreService.saveOutStore(outStore);
        return result;
    }

    @Autowired
    private StoreService storeService;

     //查询所有仓库/outstore/store-list
    @RequestMapping("/store-list")
    public Result storeList(){
        List<Store> storeList = storeService.queryAllStore();
        return Result.ok(storeList);
    }


    //查询所有出库单并分页 或者 根据仓库id 商品名称 起止时间 是否出库查询出库单并分页  outstore/outstore-page-list
    @RequestMapping("/outstore-page-list")
    public Result outStoreListPage(OutStore outStore, Page page){
        page = outStoreService.queryOutStorePage(outStore, page);
        return Result.ok(page);
    }

    //确认出库的方法/outstore/outstore-confirm
    @RequestMapping("/outstore-confirm")
    public Result confirmOutStore(@RequestBody OutStore outStore){
        Result result = outStoreService.outStoreConfirm(outStore);
        return result;
    }

}
