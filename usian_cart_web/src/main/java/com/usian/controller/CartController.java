package com.usian.controller;

import com.usian.feign.CartServiceFeign;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItem;
import com.usian.utis.CookieUtils;
import com.usian.utis.JsonUtils;
import com.usian.utis.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/frontend/cart")
public class CartController {

    @Value("${CART_COOKIE_KEY}")
    private String CART_COOKIE_KEY;
    @Value("${CART_COOKIE_EXPIRE}")
    private Integer CART_COOKIE_EXPIRE;

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @Autowired
    private CartServiceFeign cartServiceFeign;


    @RequestMapping("/addItem")
    public Result addItem(Long itemId, String userId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response){

        try {
            if(StringUtils.isBlank(userId)){
                //没有登录
                //1.从cookie中查询商品列表
                Map<String, TbItem> cart =  getCartFromCookie(request);

                //2.添加商品到购物车
                addItemToCart(cart,itemId,num);

                //3.把购物车商品列表写入cookie
                addClientCookie(cart,request,response);
            }else {
                //已登录
                //查询购物车列表
                Map<String, TbItem> cart =  getCartFromRedis(userId);
                //添加商品到购物车
                addItemToCart(cart,itemId,num);
                //把购物车写到redis
             Boolean  addCartToRedis  = addCartToRedis(cart,userId);
             if(!addCartToRedis){
                 return Result.error("添加失败");
             }

            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("error");
        }
    }

    private Boolean addCartToRedis(Map<String, TbItem> cart, String userId) {
          return   cartServiceFeign.insertCart(cart,userId);
    }

    private Map<String, TbItem> getCartFromRedis(String userId) {
       Map<String,TbItem> cart= cartServiceFeign.selectCartByUserId(userId);
       if(cart!=null && cart.size()>0){
           return cart;
       }
        return new HashMap<String,TbItem>();
    }


    //3.把购物车商品列表写入cookie
    private void addClientCookie( Map<String, TbItem> cart,HttpServletRequest request, HttpServletResponse response) {
        String cartJson = JsonUtils.objectToJson(cart);
        CookieUtils.setCookie(request,response,CART_COOKIE_KEY,cartJson,CART_COOKIE_EXPIRE,true);
    }

    //2.添加商品到购物车
    private void addItemToCart(Map<String, TbItem> cart, Long itemId, Integer num) {
        TbItem tbItem = cart.get(itemId.toString());
        if(tbItem!=null){
            tbItem.setNum(tbItem.getNum()+num);
        }else{
            tbItem = itemServiceFeign.selectItemInfo(itemId);
            tbItem.setNum(num);
        }
        cart.put(itemId.toString(),tbItem);
    }

    //1.从cookie中查询商品列表
    private Map<String, TbItem> getCartFromCookie(HttpServletRequest request) {
        String cartJsom = CookieUtils.getCookieValue(request, CART_COOKIE_KEY, true);
        if(StringUtils.isNotBlank(cartJsom)){
            Map<String,TbItem> map = JsonUtils.jsonToMap(cartJsom, TbItem.class);
            return map;
        }
            return  new HashMap<String, TbItem>();
    }

    //查看购物车
    @RequestMapping("/showCart")
    public Result showCart(String userId,HttpServletRequest request){
        try {
            List<TbItem> tbItemList = new ArrayList<TbItem>();
            if(StringUtils.isBlank(userId)){
                //未登录
                Map<String, TbItem> cart = getCartFromCookie(request);
                Set<String> keySet = cart.keySet();
                for (String itemId : keySet) {
                    TbItem tbItem = cart.get(itemId);
                    tbItemList.add(tbItem);
                }
            }else {
                //登录成功
                Map<String, TbItem> cart = getCartFromRedis(userId);
                Set<String> keySet = cart.keySet();
                for (String itemId : keySet) {
                    TbItem tbItem = cart.get(itemId);
                    tbItemList.add(tbItem);
                }
            }
            return Result.ok(tbItemList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("error");
    }

    //修改购物车
    @RequestMapping("/updateItemNum")
    public Result updateItemNum(String userId,Long itemId,Integer num,HttpServletRequest request, HttpServletResponse response){

        try {
            if(StringUtils.isBlank(userId)){
                //未登录
                //1.获得cookie中的购物车
                Map<String, TbItem> cart = getCartFromCookie(request);

                //2.修改购物车中的商品
                TbItem tbItem = cart.get(itemId.toString());
                tbItem.setNum(num);
                cart.put(itemId.toString(),tbItem);
                //3.把购物车写到cookie
                addClientCookie(cart,request,response);
            }else {
                //以登录
                Map<String, TbItem> cart = getCartFromRedis(userId);
                TbItem tbItem = cart.get(itemId.toString());
                tbItem.setNum(num);
                cart.put(itemId.toString(),tbItem);
                addCartToRedis(cart,userId);
            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("修改失败");
    }

    //删除购物车
    @RequestMapping("/deleteItemFromCart")
    public Result deleteItemFromCart(String userId,Long itemId,Integer num,HttpServletRequest request,HttpServletResponse response){

        try {
            if(StringUtils.isBlank(userId)){
                //未登录
                Map<String, TbItem> cart = getCartFromCookie(request);
                cart.remove(itemId.toString());
                addClientCookie(cart,request,response);
            }else {
                //以登录
                Map<String, TbItem> cart = getCartFromRedis(userId);
                cart.remove(itemId.toString());
                addCartToRedis(cart,userId);
            }
            return  Result.ok();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("删除失败");
    }
}
