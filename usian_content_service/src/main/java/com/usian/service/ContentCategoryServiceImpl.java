package com.usian.service;

import com.usian.mapper.TbContentCategoryMapper;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(id);

        return tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
    }

    //内容分类添加
    @Override
    public Integer insertContentCategory(TbContentCategory tbContentCategory) {
        //添加默认数据
        Date date = new Date();
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(date);
        tbContentCategory.setUpdated(date);
        Integer insertSelective = tbContentCategoryMapper.insertSelective(tbContentCategory);

        //查询是否有父节点
        TbContentCategory parentTbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        //判断当父前节点是否有字节的
        if(!parentTbContentCategory.getIsParent()){
            parentTbContentCategory.setIsParent(true);
            parentTbContentCategory.setUpdated(new Date());
            Integer updateByPrimaryKeySelective = tbContentCategoryMapper.updateByPrimaryKeySelective(parentTbContentCategory);
        }

        return insertSelective;
    }

    //内容分类删除
    @Override
    public Integer deleteContentCategoryById(Long categoryId) {
        //查询当前节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        //如果是父节点不能删除
        if(tbContentCategory.getIsParent()==true){
            return 0;
        }
        //删除当前节点
        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);
        //与如果他不是父节点 就把他改成不是父节点
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(tbContentCategory.getParentId());
        List<TbContentCategory> tbContentCategoryList = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        if(tbContentCategoryList==null || tbContentCategoryList.size()==0){
            TbContentCategory parentTbContentCategory = new TbContentCategory();
            parentTbContentCategory.setId(tbContentCategory.getParentId());
            parentTbContentCategory.setIsParent(false);
            parentTbContentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKeySelective(parentTbContentCategory);
        }

        return 200;
    }

    //内容分类修改
    @Override
    public Integer updateContentCategory(TbContentCategory tbContentCategory) {
        tbContentCategory.setUpdated(new Date());
        int updateByPrimaryKeySelective = tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        return updateByPrimaryKeySelective;
    }
}
