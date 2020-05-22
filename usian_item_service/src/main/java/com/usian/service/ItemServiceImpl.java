package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemExample;
import com.usian.pojo.TbItemParamItem;
import com.usian.utis.IDUtils;
import com.usian.utis.PageResult;
import org.bouncycastle.crypto.tls.TlsDHUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Override
    public TbItem selectItemInfo(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Long rows) {
        PageHelper.startPage(page,rows.intValue());
        TbItemExample tbItemExample = new TbItemExample();
        tbItemExample.setOrderByClause("updated DESC");
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
        for (int i = 0; i < tbItemList.size(); i++) {
            TbItem tbItem =  tbItemList.get(i);
            tbItem.setPrice(tbItem.getPrice()/100);
        }
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItemList);
        PageResult result = new PageResult();
        result.setPageIndex(pageInfo.getPageNum());
        result.setTotalPage(pageInfo.getTotal());
        result.setResult(tbItemList);
        return result;
    }

    @Override
    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        //1.保存商品信息
        long itemId = IDUtils.genItemId();
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        tbItem.setPrice(tbItem.getPrice()*100);
        int tbItemNum = tbItemMapper.insertSelective(tbItem);
        //2.保存商品描述信息
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        int tbItemDescNum = tbItemDescMapper.insertSelective(tbItemDesc);
        //3.保存商品规格信息
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setUpdated(date);
        int tbItemParamItemNum= tbItemParamItemMapper.insertSelective(tbItemParamItem);
        return tbItemNum+tbItemDescNum+tbItemParamItemNum;
    }

    @Override
    public Integer deleteItemById(Long itemId) {
        return tbItemMapper.deleteByPrimaryKey(itemId);
    }
}
