package com.usian.controller;

import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItem;
import com.usian.utis.PageResult;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/item")
public class ItemController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    /*
    *查询商品详情
    *
    * */
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId){
        TbItem tbItem = itemServiceFeign.selectItemInfo(itemId);
        if(tbItem !=null){
            return Result.ok(tbItem);
        }
        return Result.error("没有结果");
    }

    /*
    * 分页查询商品列表
    *
    * */
    @RequestMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "2") Long rows){
        PageResult pageResult = itemServiceFeign.selectTbItemAllByPage(page,rows);
        if(pageResult.getResult()!=null && pageResult.getResult().size()>0){
            return Result.ok(pageResult);
        }
        return Result.error("没有结果");
    }

    @RequestMapping("/insertTbItem")
    public Result insertTbItem(TbItem tbItem,String desc,String itemParams){
       Integer result =  itemServiceFeign.insertTbItem(tbItem,desc,itemParams);
            if(result==3){
                return Result.ok();
            }
        return Result.error("添加失败");
    }

    //商品删除
    @RequestMapping("/deleteItemById")
    public Result deleteItemById(Long itemId){
       Integer deleteItemById = itemServiceFeign.deleteItemById(itemId);
       if(deleteItemById==1){
           return  Result.ok();
       }
        return Result.error("删除失败");
    }
}
