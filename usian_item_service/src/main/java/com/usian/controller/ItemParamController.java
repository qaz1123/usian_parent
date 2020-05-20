package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import com.usian.utis.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/itemParam")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    /*
     * 根据商品分类 ID 查询规格参数模板
     * */
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable Long itemCatId){
        return  itemParamService.selectItemParamByItemCatId(itemCatId);
    }

    /*
     * 商品规格参数查询
     * */
    @RequestMapping("/selectItemParamAll")
    public PageResult selectItemParamAll(Integer page,Integer rows){
        return  itemParamService.selectItemParamAll(page,rows);
    }

    /*
     * 添加商品规格
     * */
    @RequestMapping("/insertItemParam")
    public Integer insertItemParam(Long itemCatId,String paramData){
        return itemParamService.insertItemParam(itemCatId,paramData);
    }
}
