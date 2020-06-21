package com.usian.proxy.staticProxy;

public class Client {
    public static void main(String[] args) {
        Star star = new RealStar();
        Star proxy= new ProxyStar(star);
            proxy.bookTicket();
            proxy.collectMoney();
            proxy.confer();
            proxy.signContract();
            proxy.sing();
    }
}
