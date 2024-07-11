package com.pn.controller;

import com.pn.entity.Result;
import com.pn.service.StoreService;
import com.pn.vo.StoreCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/statistics")
@RestController
public class StatisticsController {
    @Autowired
    private StoreService storeService;

    //统计仓库库存的url接口   /statistics/store-invent -- 查询每个仓库存储的商品的数量;
    @RequestMapping("/store-invent")
    public Result storeInvent(){
        List<StoreCountVo> storeCountVoList = storeService.queryStoreCount();
        return Result.ok(storeCountVoList);
    }
}
