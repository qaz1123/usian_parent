package com.usian.proxy.dynamicProxy;

import com.sun.tools.javac.jvm.Code;

public class RealCoder implements Coder {
    @Override
    public void signContract() {
        System.out.println("RealCoder.signContract");
    }

    @Override
    public void code() {
        System.out.println("小明在cpdd.code");
    }

    @Override
    public void collectMoney() {
        System.out.println("RealCoder.collectMoney");
    }
}
