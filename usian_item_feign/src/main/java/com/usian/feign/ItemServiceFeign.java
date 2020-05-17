package com.usian.feign;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.utis.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "usian-item-service")
public interface ItemServiceFeign {


    @RequestMapping("/service/item/selectItemInfo")
    public TbItem selectItemInfo(@RequestParam Long itemId);

    @RequestMapping("/service/item/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(@RequestParam("page") Integer page,
                                            @RequestParam("rows") Long rows);

    @RequestMapping("/service/itemCat/selectItemCategoryByParentId")
    List<TbItemCat> selectItemCategoryByParentId(@RequestParam Long id);
}
