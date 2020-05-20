package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamExample;
import com.usian.utis.PageResult;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
        if(tbItemParamList!=null && tbItemParamList.size()>0){
                return  tbItemParamList.get(0);
        }
                return  null;
    }

    @Override
    public PageResult selectItemParamAll(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        tbItemParamExample.setOrderByClause("updated DESC");
        List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
        PageInfo<TbItemParam> pageInfo = new PageInfo<TbItemParam>(tbItemParamList);
        PageResult pageResult = new PageResult();
        pageResult.setTotalPage(pageInfo.getTotal());
        pageResult.setResult(pageInfo.getList());
        pageResult.setPageIndex(pageInfo.getPageNum());


        return pageResult;
    }

    @Override
    public Integer insertItemParam(Long itemCatId, String paramData) {
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExample(tbItemParamExample);
        if(tbItemParamList.size()>0){
            return  0;
        }
        TbItemParam tbItemParam = new TbItemParam();
        Date date = new Date();
        tbItemParam.setItemCatId(itemCatId);
        tbItemParam.setParamData(paramData);
        tbItemParam.setUpdated(date);
        tbItemParam.setCreated(date);
        return tbItemParamMapper.insertSelective(tbItemParam);
    }
}
