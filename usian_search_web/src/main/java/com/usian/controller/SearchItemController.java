package com.usian.controller;

import com.usian.feign.SearchItemServiceFeign;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
