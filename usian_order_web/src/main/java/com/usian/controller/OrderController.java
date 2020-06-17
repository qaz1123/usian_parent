package com.usian.controller;

import com.usian.feign.CartServiceFeign;
import com.usian.feign.OrderServiceFeign;
import com.usian.pojo.OrderInfo;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;
import com.usian.utis.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend/order")
public class OrderController {

    @Autowired
    private CartServiceFeign cartServiceFeign;

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @RequestMapping("/goSettlement")
    public Result goSettlement(String[] ids, String userId){
        List<TbItem> tbItemList = new ArrayList<TbItem>();
        Map<String, TbItem> cart = cartServiceFeign.selectCartByUserId(userId);
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            tbItemList.add(cart.get(id));
        }
        if(tbItemList.size()>0){
            return Result.ok(tbItemList);
        }
        return  Result.error("查询失败");
    }

    @RequestMapping("/insertOrder")
    public Result insertOrder(String orderItem, TbOrder tbOrder, TbOrderShipping tbOrderShipping){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderItem(orderItem);
        orderInfo.setTbOrder(tbOrder);
        orderInfo.setTbOrderShipping(tbOrderShipping);
       String order = orderServiceFeign.insertOrder(orderInfo);
       if(order!=null){
           return Result.ok(order);
       }
            return Result.error("订单提交失败");
    }

}
