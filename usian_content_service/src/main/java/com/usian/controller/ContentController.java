package com.usian.controller;

import com.usian.pojo.TbContent;
import com.usian.service.ContentService;
import com.usian.utis.AdNode;
import com.usian.utis.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    //内容查询
    @RequestMapping("/selectTbContentAllByCategoryId")
    public PageResult selectTbContentAllByCategoryId(Integer page,Integer rows,Long categoryId){
        return contentService.selectTbContentAllByCategoryId(page,rows,categoryId);
    }

    //内容添加
    @RequestMapping("/insertTbContent")
    public Integer insertTbContent(@RequestBody TbContent tbContent){
        return  contentService.insertTbContent(tbContent);
    }

    //内容删除
    @RequestMapping("/deleteContentByIds")
    public Integer deleteContentByIds( Long ids){
        return  contentService.deleteContentByIds(ids);
    }


    //首页大广告查询
    @RequestMapping("/selectFrontendContentByAD")
    public  List<AdNode> selectFrontendContentByAD(){
        return  contentService.selectFrontendContentByAD();
    }

}
