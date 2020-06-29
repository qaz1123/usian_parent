package com.usian.fallback;

import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.*;
import com.usian.utis.CatResult;
import com.usian.utis.PageResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ItemServiceFallback implements FallbackFactory<ItemServiceFeign> {
    @Override
    public ItemServiceFeign create(Throwable throwable) {
        return new ItemServiceFeign() {
            @Override
            public TbItem selectItemInfo(Long itemId) {
                return null;
            }

            @Override
            public PageResult selectTbItemAllByPage(Integer page, Long rows) {
                return null;
            }

            @Override
            public List<TbItemCat> selectItemCategoryByParentId(Long id) {
                return null;
            }

            @Override
            public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
                return null;
            }

            @Override
            public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
                return null;
            }

            @Override
            public PageResult selectItemParamAll(Integer page, Integer rows) {
                return null;
            }

            @Override
            public Integer insertItemParam(Long itemCatId, String paramData) {
                return null;
            }

            @Override
            public Integer deleteItemParamById(Long id) {
                return null;
            }

            @Override
            public Integer deleteItemById(Long itemId) {
                return null;
            }

            @Override
            public CatResult selectItemCategoryAll() {
                return null;
            }

            @Override
            public Map<String, Object> preUpdateItem(Long itemId) {
                return null;
            }

            @Override
            public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
                return null;
            }

            @Override
            public TbItemDesc selectItemDescByItemId(Long itemId) {
                return null;
            }

            @Override
            public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
                return null;
            }
        };
    }
}
