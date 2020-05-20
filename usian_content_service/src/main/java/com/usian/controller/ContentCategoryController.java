package com.usian.controller;

import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/content")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    //根据Id内容分类查询
    @RequestMapping("/selectContentCategoryByParentId")
    public List<TbContentCategory> selectContentCategoryByParentId( Long id){
        return contentCategoryService.selectContentCategoryByParentId(id);
    }

}
