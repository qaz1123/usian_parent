package com.usian.proxy.staticProxy;

public class ProxyStar implements Star {

    private Star star;

    public ProxyStar(Star star){
        this.star=star;
    }

    @Override
    public void confer() {
        System.out.println("ProxyStar.confer");
    }

    @Override
    public void signContract() {
        System.out.println("ProxyStar.signContract");
    }

    @Override
    public void bookTicket() {
        System.out.println("ProxyStar.bookTicket");
    }

    @Override
    public void sing() {
        System.out.println("方法执行前");
        star.sing();
        System.out.println("方法执行后");
    }

    @Override
    public void collectMoney() {
        System.out.println("ProxyStar.collectMoney");
    }
}
