package com.usian.service;

import com.usian.pojo.TbContent;
import com.usian.utis.PageResult;

public interface ContentService {
    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId);

    Integer insertTbContent(TbContent tbContent);

    Integer deleteContentByIds(Long ids);
}
