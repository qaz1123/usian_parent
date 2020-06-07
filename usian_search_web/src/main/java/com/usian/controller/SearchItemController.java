package com.usian.controller;

import com.usian.feign.SearchItemServiceFeign;
import com.usian.pojo.SearchItem;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/searchItem")
public class SearchItemController {

    @Autowired
    private SearchItemServiceFeign searchItemServiceFeign;

    @RequestMapping("/importAll")
    public Result importAll(){
      Boolean importAll  = searchItemServiceFeign.importAll();
      if(importAll){
          return Result.ok();
      }
        return Result.error("导入失败");
    }

    @RequestMapping("/list")
    public List<SearchItem> selectByQ(String q, @RequestParam(defaultValue = "1") Long page,
                                      @RequestParam(defaultValue = "20") Integer pageSize){
        return  searchItemServiceFeign.selectByQ(q,page,pageSize);

    }
}
