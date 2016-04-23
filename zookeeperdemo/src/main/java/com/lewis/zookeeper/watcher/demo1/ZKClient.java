package com.lewis.zookeeper.watcher.demo1;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKClient {
    private static final String connectString = "localhost:2181";
    private static final int timeout =3000;
    private static ZooKeeper zk  = null;
    public ZKClient(String connectString, int timeout, Watcher watcher){
        try {
            zk = new ZooKeeper(connectString,timeout,watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testWatcherDisabled(String path) throws KeeperException, InterruptedException {
        LewisWather wather1 = new LewisWather();
        wather1.setZk(zk);
        System.out.println("enter testWatcherDisabled...");
        zk.getData(path,wather1,null);
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String path ="/node1";
        LewisWather watcher = new LewisWather();
        final CountDownLatch latch = new CountDownLatch(1);
        ZKClient zkClient = new ZKClient(connectString,timeout,watcher);
        Thread notifier = new Thread(){
            @Override
            public void run() {
                while (zk.getState() != ZooKeeper.States.CONNECTED) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                latch.countDown();
            }
        };
        notifier.start();
        latch.await();
        zkClient.testWatcherDisabled(path);
        Thread.sleep(440000000);
    }
}
