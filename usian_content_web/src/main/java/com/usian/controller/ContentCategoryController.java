package com.usian.controller;

import com.usian.feign.ContentServiceFeign;
import com.usian.pojo.TbContentCategory;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/content")
public class ContentCategoryController {

    @Autowired
    private ContentServiceFeign contentServiceFeign;

    //根据Id内容分类查询
    @RequestMapping("/selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(defaultValue = "0") Long id){
      List<TbContentCategory> tbContentCategoryList = contentServiceFeign.selectContentCategoryByParentId(id);
      if(tbContentCategoryList!=null && tbContentCategoryList.size()>0){
          return Result.ok(tbContentCategoryList);
      }
      return Result.error("查无结果");
    }

    //内容分类添加
    @RequestMapping("/insertContentCategory")
    public Result insertContentCategory(TbContentCategory tbContentCategory){
      Integer insert =   contentServiceFeign.insertContentCategory(tbContentCategory);
      if(insert==1){
          return  Result.ok();
      }
      return  Result.error("添加失败");
    }

    //内容分类删除
    @RequestMapping("/deleteContentCategoryById")
    public Result deleteContentCategoryById(Long categoryId){
     Integer  deleteContentCategoryById =   contentServiceFeign.deleteContentCategoryById(categoryId);
     if(deleteContentCategoryById==200){
         return Result.ok();
     }
     return Result.error("删除失败");
    }

    //内容分类修改
    @RequestMapping("/updateContentCategory")
    public Result updateContentCategory(TbContentCategory tbContentCategory){
        Integer updateContentCategory = contentServiceFeign.updateContentCategory(tbContentCategory);
        if(updateContentCategory==1){
            return Result.ok();
        }
        return  Result.error("修改失败");
    }
}
