package com.usian.controller;

import com.usian.pojo.SearchItem;
import com.usian.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/search")
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/importAll")
    public Boolean importAll(){
       return searchItemService.importAll();
    }
}
