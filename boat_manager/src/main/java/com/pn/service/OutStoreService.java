package com.pn.service;

import com.pn.entity.OutStore;
import com.pn.entity.Result;
import com.pn.page.Page;

public interface OutStoreService {
    //添加出库单的业务方法
    Result saveOutStore(OutStore outStore);
    //分页查询出库单
    Page queryOutStorePage(OutStore outStore,Page page);
    //确认出库的业务方法
    Result outStoreConfirm(OutStore outStore);
}
