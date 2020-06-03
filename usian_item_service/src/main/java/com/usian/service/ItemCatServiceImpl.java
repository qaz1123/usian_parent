package com.usian.service;

import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;

import com.usian.redis.RedisClient;
import com.usian.utis.CatNode;
import com.usian.utis.CatResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Value("${PROTAL_CATRESULT_KEY}")
    private String PROTAL_CATRESULT_KEY;

    @Autowired
    private RedisClient redisClient;

    @Override
    public List<TbItemCat> selectItemCategoryByParentId(Long id) {
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andStatusEqualTo(1);
        criteria.andParentIdEqualTo(id);
        return tbItemCatMapper.selectByExample(tbItemCatExample);
    }

    //商品分类菜单查询
    @Override
    public CatResult selectItemCategoryAll() {
        CatResult catResultRedis = (CatResult) redisClient.get(PROTAL_CATRESULT_KEY);
        if(catResultRedis!=null){
            return  catResultRedis;
        }
        CatResult catResult = new CatResult();
        catResult.setData(getCatList(0L));
        redisClient.set(PROTAL_CATRESULT_KEY,catResult);

        return catResult;
    }

    private List<?> getCatList(long parentId) {
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCatList = tbItemCatMapper.selectByExample(tbItemCatExample);
        List list = new ArrayList();
        int count = 0;
        for (int i = 0; i < tbItemCatList.size(); i++) {
            TbItemCat tbItemCat =  tbItemCatList.get(i);
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                catNode.setName(tbItemCat.getName());
                catNode.setItem(getCatList(tbItemCat.getId()));
                list.add(catNode);
                count = count+1;
                if(count==18){
                    break;
                }
            }else {
                list.add(tbItemCat.getName());
            }
        }
        return list;
    }
}
