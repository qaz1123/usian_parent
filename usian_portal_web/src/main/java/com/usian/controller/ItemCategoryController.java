package com.usian.controller;

import com.usian.feign.ItemServiceFeign;
import com.usian.utis.CatResult;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/itemCategory")
public class ItemCategoryController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    //商品分类菜单查询
    @RequestMapping("/selectItemCategoryAll")
    public Result selectItemCategoryAll(){
     CatResult  catResult= itemServiceFeign.selectItemCategoryAll();
     if(catResult.getData().size()>0){
         return Result.ok(catResult);
     }
     return Result.error("查询失败");
    }

}
