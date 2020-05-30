package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentExample;
import com.usian.redis.RedisClient;
import com.usian.utis.AdNode;
import com.usian.utis.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    @Value("${AD_CATEGORY_ID}")
    private Long AD_CATEGORY_ID;

    @Value("${AD_HEIGHT}")
    private Integer AD_HEIGHT;

    @Value("${AD_WIDTH}")
    private Integer AD_WIDTH;

    @Value("${AD_HEIGHTB}")
    private Integer AD_HEIGHTB;

    @Value("${AD_WIDTHB}")
    private Integer AD_WIDTHB;

    @Value("${PORTAL_AD_KEY}")
    private String PORTAL_AD_KEY;

    @Autowired
    private RedisClient redisClient;


    @Autowired
    private TbContentMapper tbContentMapper;

    //内容查询
    @Override
    public PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId) {
        PageHelper.startPage(page,rows);
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //tbContentExample.setOrderByClause("updated DESC");
        List<TbContent> tbContentsList = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContentsList);
        PageResult pageResult = new PageResult();
        pageResult.setResult(pageInfo.getList());
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotalPage(Long.valueOf(pageInfo.getPages()));
        return pageResult;
    }

    //内容添加
    @Override
    public Integer insertTbContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        redisClient.hdel(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
        return tbContentMapper.insertSelective(tbContent);
    }

    //内容删除
    @Override
    public Integer deleteContentByIds(Long ids) {
        redisClient.hdel(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
        return tbContentMapper.deleteByPrimaryKey(ids);
    }

    //首页大广告查询
    @Override
    public List<AdNode> selectFrontendContentByAD() {
        List<AdNode> adNodeListRedis = (List<AdNode>) redisClient.hget(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
        if(adNodeListRedis!=null){
            return  adNodeListRedis;
        }
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(AD_CATEGORY_ID);
        List<TbContent> tbContentList = tbContentMapper.selectByExample(tbContentExample);
        List<AdNode> adNodeList = new ArrayList<>();
        for (TbContent tbContent : tbContentList) {
            AdNode adNode = new AdNode();
            adNode.setSrc(tbContent.getPic());
            adNode.setSrcB(tbContent.getPic2());
            adNode.setHref(tbContent.getUrl());
            adNode.setHeight(AD_HEIGHT);
            adNode.setWidth(AD_WIDTH);
            adNode.setHeightB(AD_HEIGHTB);
            adNode.setWidthB(AD_WIDTHB);
            adNodeList.add(adNode);
        }
        redisClient.hset(PORTAL_AD_KEY,AD_CATEGORY_ID.toString(),adNodeList);
        return adNodeList;
    }
}
