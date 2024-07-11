package com.pn.service;

import com.pn.entity.InStore;
import com.pn.entity.Result;
import com.pn.page.Page;

public interface InStoreService {

    //添加入库单的业务方法
    Result saveInStore(InStore inStore,Integer buyId);
    //分页查询入库单的业务员方法
    Page queryInStorePage(InStore inStore,Page page);

    //确认入库的业务方法
    Result inStoreConfirm(InStore inStore);
}
