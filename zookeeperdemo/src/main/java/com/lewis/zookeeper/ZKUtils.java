package com.lewis.zookeeper;

import org.apache.zookeeper.ZooKeeper;
import java.util.concurrent.CountDownLatch;


public class ZKUtils {

    //该方法确保zk客户端连接上zk服务器，否则调用该方法的线程则一直等待
    public static void confirmConnected(final ZooKeeper zk) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Thread notifier = new Thread(){
            @Override
            public void run() {
                try {
                    while (zk.getState() != ZooKeeper.States.CONNECTED) {
                        Thread.sleep(10);
                    }
                } catch (Exception e) {

                }finally {
                    latch.countDown();
                }
            }
        };
        notifier.start();
        latch.await();
    }
}
