package com.fehead;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2019-11-03 14:51
 * @Version 1.0
 */
public class Son extends Parent implements Plane{
    long money=1999993L;

    @Override
    public void fly() {
        System.out.println("son 飞行");
    }
}
