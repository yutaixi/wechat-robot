package com.im.test;

import java.util.concurrent.atomic.AtomicInteger;

public class VoatileTest {
    public volatile int inc = 0;

    public volatile AtomicInteger aint=new AtomicInteger(0);
    
    public synchronized void increase() {
        inc++;
        aint.incrementAndGet();
    }

    public static void main(String[] args) {
        final VoatileTest test = new VoatileTest();
        for(int i=0;i<10;i++){
            new Thread(){
                public void run() {
                    for(int j=0;j<1000;j++)
                        test.increase();
                };
            }.start();
        }

        while(Thread.activeCount()>1)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.inc);
        System.out.println(test.aint);
    }
}
