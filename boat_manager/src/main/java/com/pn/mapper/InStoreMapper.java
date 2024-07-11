package com.pn.mapper;

import com.pn.entity.InStore;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InStoreMapper {

    //添加入库单
    int insertInStore(InStore inStore);

    //获取入库列表总行数
    Integer findInStoreCount(InStore inStore);
    //查询所有入库单并分页 或者 根据仓库id、商品名称、起止时间查询入库单并分页
    List<InStore> findInStorePage(@Param("inStore") InStore inStore, @Param("page") Page page);

    //根据入库单id修改入库单状态为已入库
    int setIsInById(Integer inStoreId);

}
