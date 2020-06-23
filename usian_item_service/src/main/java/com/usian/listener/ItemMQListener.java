package com.usian.listener;

import com.usian.pojo.DeDuplication;
import com.usian.pojo.LocalMessage;
import com.usian.service.DeDuplicationService;
import com.usian.service.ItemService;
import com.usian.utis.JsonUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Component
public class ItemMQListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private DeDuplicationService deDuplicationService;

    @RabbitListener(bindings =@QueueBinding(
            value = @Queue(value = "item_queue",durable = "true"),
            exchange = @Exchange(value = "order_exchange",type = ExchangeTypes.TOPIC),
            key = {"order.*"}
    ))

    public void list(String msg,Channel channel, Message message) throws IOException {
        LocalMessage localMessage = JsonUtils.jsonToPojo(msg, LocalMessage.class);
        //进行幂等判断
     DeDuplication deDuplication =  deDuplicationService.selectDeDuplicationByTxNo(localMessage.getTxNo());
      if(deDuplication==null){
          //扣减库存
          Integer result  =itemService.updateTbItemByOrderId(localMessage.getOrderNo());
          if(!(result>0)){
              throw new RuntimeException("扣减失败");
          }
          //记录成功执行过的事务
          deDuplicationService.insertDeDuplication(localMessage.getTxNo());
      }else {
          System.out.println("=======幂等生效：事务"+deDuplication.getTxNo() +" 已成功执行===========");
      }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
      }

    }
