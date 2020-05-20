package com.usian.controller;

import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItemParam;
import com.usian.utis.PageResult;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    /*
    * 根据商品分类 ID 查询规格参数模板
    * */
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable Long itemCatId){
        TbItemParam tbItemParam = itemServiceFeign.selectItemParamByItemCatId(itemCatId);
        if(tbItemParam != null){
            return Result.ok(tbItemParam);
        }
        return Result.error("查无结果");
    }

    /*
    * 商品规格参数查询
    * */
    @RequestMapping("/selectItemParamAll")
    public Result selectItemParamAll(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "3") Integer rows){
       PageResult pageResult =  itemServiceFeign.selectItemParamAll(page,rows);
       if(pageResult.getResult().size()>0){
           return Result.ok(pageResult);
       }
       return   Result.error("错误");
    }

    /*
    * 添加商品规格
    * */
    @RequestMapping("/insertItemParam")
    public Result insertItemParam(Long itemCatId,String paramData){
        Integer  num = itemServiceFeign.insertItemParam(itemCatId,paramData);
        if(num==1){
            return  Result.ok();
        }
        return Result.error("添加失败了");
    }

    //删除商品规格
    @RequestMapping("/deleteItemParamById")
    public Result deleteItemParamById(Long id){
     Integer deNum =    itemServiceFeign.deleteItemParamById(id);
     if(deNum==1){
         return Result.ok();
     }
     return  Result.error("删除失败");
    }
}
