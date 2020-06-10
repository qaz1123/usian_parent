package com.usian.service;

import com.usian.pojo.TbItem;

import com.usian.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface CartService {



    Map<String, TbItem> selectCartByUserId(String userId);

    Boolean insertCart(Map<String, TbItem> cart, String userId);
}
