package com.usian.service;

import com.usian.pojo.TbItemParam;
import com.usian.utis.PageResult;

public interface ItemParamService {
    TbItemParam selectItemParamByItemCatId(Long itemCatId);

    PageResult selectItemParamAll(Integer page, Integer rows);

    Integer insertItemParam(Long itemCatId, String paramData);
}
