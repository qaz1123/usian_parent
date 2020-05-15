package com.usian.service;

import com.usian.pojo.TbItem;
import com.usian.utis.PageResult;

public interface ItemService {
    TbItem selectItemInfo(Long itemId);

    PageResult selectTbItemAllByPage(Integer page, Long rows);
}
