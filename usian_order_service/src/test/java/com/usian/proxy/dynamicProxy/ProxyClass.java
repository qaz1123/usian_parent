package com.usian.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyClass implements InvocationHandler {

    private Object realStar;

    public ProxyClass(Object object){
        this.realStar=object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("面谈，签合同，预付款，订机票");
        Object result = method.invoke(realStar, args);//反射
        System.out.println("收尾款");
        return result;
    }
}
