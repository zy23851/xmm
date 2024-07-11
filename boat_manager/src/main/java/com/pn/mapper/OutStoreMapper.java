package com.pn.mapper;

import com.pn.entity.OutStore;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutStoreMapper {

    //添加出库单的方法
    int insertOutStore(OutStore outStore);

    //查询总行数
    Integer findOutStoreCount(OutStore outStore);
    //查询所有出库单并分页 或者 根据仓库id 商品名称 起止时间 是否出库查询出库单并分页
    List<OutStore> findOutStorePage(@Param(("outStore")) OutStore outStore, @Param("page") Page page);

    //根据出库id修改出库状态为已出库
    int setIsOutById(Integer outsId);
}
