package com.pn.service.impl;

        import com.pn.entity.InStore;
        import com.pn.entity.Result;
        import com.pn.mapper.InStoreMapper;
        import com.pn.mapper.ProductMapper;
        import com.pn.mapper.PurchaseMapper;
        import com.pn.page.Page;
        import com.pn.service.InStoreService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;

@Service
public class InStoreServiceImpl implements InStoreService {

    @Autowired
    private InStoreMapper inStoreMapper;
    @Autowired
    private PurchaseMapper purchaseMapper;


    //添加入库单的业务方法
    @Transactional
    @Override
    public Result saveInStore(InStore inStore, Integer buyId) {
        int i = inStoreMapper.insertInStore(inStore);
        if(i>0){
            int j = purchaseMapper.setIsInById(buyId);
            if(j>0){
                return Result.ok("入库单添加成功！");
            }
            return Result.err(Result.CODE_ERR_BUSINESS,"入库单添加失败！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"入库单添加失败！");
    }


    //分页查询入库单的业务员方法
    @Override
    public Page queryInStorePage(InStore inStore, Page page) {
        List<InStore> inStorePage = inStoreMapper.findInStorePage(inStore, page);
        Integer count = inStoreMapper.findInStoreCount(inStore);
        page.setTotalNum(count);
        page.setResultList(inStorePage);
        return page;
    }


    @Autowired
    private ProductMapper productMapper;
    //确认入库的业务方法
    @Transactional
    @Override
    public Result inStoreConfirm(InStore inStore) {
        //修改入库单状态
        int i = inStoreMapper.setIsInById(inStore.getInsId());
        if(i>0){
            int j = productMapper.setInventById(inStore.getProductId(), inStore.getInNum());
            if(j>0){
                return Result.ok("入库单确认成功！");
            }
            return Result.err(Result.CODE_ERR_BUSINESS,"入库单确认失败！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"入库单确认失败！");
    }

}
