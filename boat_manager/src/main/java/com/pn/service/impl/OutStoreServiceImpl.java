package com.pn.service.impl;

import com.pn.entity.OutStore;
import com.pn.entity.Result;
import com.pn.mapper.OutStoreMapper;
import com.pn.mapper.ProductMapper;
import com.pn.page.Page;
import com.pn.service.OutStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OutStoreServiceImpl implements OutStoreService {

    @Autowired
    private OutStoreMapper outStoreMapper;

    //添加出库单的业务方法
    @Override
    public Result saveOutStore(OutStore outStore) {
        int i = outStoreMapper.insertOutStore(outStore);
        if(i>0){
            return Result.ok("出库单添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"出库单添加失败！");
    }

    //分页查询出库单
    @Override
    public Page queryOutStorePage(OutStore outStore, Page page) {
        List<OutStore> outStorePageList = outStoreMapper.findOutStorePage(outStore, page);
        Integer count = outStoreMapper.findOutStoreCount(outStore);
        page.setTotalNum(count);
        page.setResultList(outStorePageList);
        return page;
    }

    @Autowired
    private ProductMapper productMapper;

    //确认出库的业务方法
    @Transactional
    @Override
    public Result outStoreConfirm(OutStore outStore) {
        int invent = productMapper.findInventById(outStore.getProductId());
        if(invent < outStore.getOutNum()){
            return Result.err(Result.CODE_ERR_BUSINESS,"商品库存不足！");
        }

        int i = outStoreMapper.setIsOutById(outStore.getOutsId());
        if(i>0){
            int j = productMapper.setInventById(outStore.getProductId(),-outStore.getOutNum());
            if(j>0){
                return Result.ok("确认出库成功！");
            }
            return Result.err(Result.CODE_ERR_BUSINESS,"确认出库失败！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"确认出库失败！");
    }


}
