package com.usian.service;

import com.usian.mapper.*;
import com.usian.mq.MQSender;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.utis.JsonUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Value("${ORDER_ID_KEY}")
    private String ORDER_ID_KEY;

    @Value("${ORDER_ID_BEGIN}")
    private Long ORDER_ID_BEGIN;

    @Value("${ORDER_ITEM_ID_KEY}")
    private String ORDER_ITEM_ID_KEY;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Autowired
    private MQSender mqSender;

    @Override
    public String insertOrder(OrderInfo orderInfo) {
        //1、解析orderInfo
        //订单
        TbOrder tbOrder = orderInfo.getTbOrder();
        //物流信息
        TbOrderShipping tbOrderShipping = orderInfo.getTbOrderShipping();
        //商品明细
        List<TbOrderItem> tbOrderItemList = JsonUtils.jsonToList(orderInfo.getOrderItem(), TbOrderItem.class);


        //2、保存订单信息
        if(!redisClient.exists(ORDER_ID_KEY)){
            redisClient.set(ORDER_ID_KEY,ORDER_ID_BEGIN);
        }
        Long orderId = redisClient.incr(ORDER_ID_KEY, 1L);
        tbOrder.setOrderId(orderId.toString());
        Date date = new Date();
        tbOrder.setCreateTime(date);
        tbOrder.setUpdateTime(date);
        tbOrder.setStatus(1);
        tbOrderMapper.insertSelective(tbOrder);

        //3、保存明细信息
        if(!redisClient.exists(ORDER_ITEM_ID_KEY)){
            redisClient.set(ORDER_ITEM_ID_KEY,0); 
        }
        for (int i = 0; i < tbOrderItemList.size(); i++) {
            Long orderItemId = redisClient.incr(ORDER_ITEM_ID_KEY, 1L);
            TbOrderItem tbOrderItem =  tbOrderItemList.get(i);
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setOrderId(orderId.toString());
            tbOrderItemMapper.insertSelective(tbOrderItem);
        }

        //4、保存物流信息
        tbOrderShipping.setOrderId(orderId.toString());
        tbOrderShipping.setCreated(date);
        tbOrderShipping.setUpdated(date);
        tbOrderShippingMapper.insertSelective(tbOrderShipping);


        //把消息存入到本地消息表
        LocalMessage localMessage = new LocalMessage();
        localMessage.setOrderNo(orderId.toString());
        localMessage.setTxNo(UUID.randomUUID().toString());
        localMessage.setState(0);
        localMessageMapper.insertSelective(localMessage);

        //发送消息，修改本地消费表状态
        mqSender.sendMsg(localMessage);

        //5、返回订单id
        return orderId.toString();
    }

    @Override
    public List<TbOrder> selectOverTimeTbOrder() {

        return tbOrderMapper.selectOvertimeOrder();
    }

    @Override
    public void updateOverTimeTbOrder(TbOrder tbOrder) {
        tbOrder.setStatus(6);
        Date date = new Date();
        tbOrder.setCloseTime(date);
        tbOrder.setEndTime(date);
        tbOrder.setUpdateTime(date);
        tbOrderMapper.updateByPrimaryKeySelective(tbOrder);
    }

    @Override
    public void updateTbItemByOrderId(String orderId) {
        TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = tbOrderItemExample.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<TbOrderItem> tbOrderItems = tbOrderItemMapper.selectByExample(tbOrderItemExample);
        for (int i = 0; i < tbOrderItems.size(); i++) {
            TbOrderItem tbOrderItem =  tbOrderItems.get(i);
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(Long.valueOf(tbOrderItem.getItemId()));
            tbItem.setNum(tbItem.getNum()+tbOrderItem.getNum());
            tbItem.setUpdated(new Date());
            tbItemMapper.updateByPrimaryKey(tbItem);
        }
    }
}
