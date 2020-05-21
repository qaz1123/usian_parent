package com.usian.controller;

import com.usian.feign.ContentServiceFeign;
import com.usian.pojo.TbContent;
import com.usian.utis.PageResult;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/content")
public class ContentController {

    @Autowired
    private ContentServiceFeign contentServiceFeign;

    //内容查询
    @RequestMapping("/selectTbContentAllByCategoryId")
    public Result selectTbContentAllByCategoryId(@RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "30") Integer rows, Long categoryId){

       PageResult pageResult =  contentServiceFeign.selectTbContentAllByCategoryId(page,rows,categoryId);
       if(pageResult.getResult().size()>0){
           return  Result.ok(pageResult);
       }
       return Result.error("查无结果");
    }

    //内容添加
    @RequestMapping("/insertTbContent")
    public Result insertTbContent(TbContent tbContent){
        Integer insertTbContent = contentServiceFeign.insertTbContent(tbContent);
        if(insertTbContent==1){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    //内容删除
    @RequestMapping("/deleteContentByIds")
    public Result deleteContentByIds(Long ids){
        Integer deleteContentByIds =contentServiceFeign.deleteContentByIds(ids);
        if(deleteContentByIds==1){
            return  Result.ok();
        }
        return Result.error("删除失败");
    }
}
