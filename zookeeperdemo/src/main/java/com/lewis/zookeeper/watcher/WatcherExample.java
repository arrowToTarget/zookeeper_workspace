package com.lewis.zookeeper.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by zhangminghua on 2016/4/18.
 */
public class WatcherExample implements Watcher{

    private ZooKeeper zk  = null;

    public void process(WatchedEvent watchedEvent) {
        System.out.println("事件触发了....begin");
        System.out.println("Watcher = "+this.getClass().getName());
        System.out.println("Path = "+watchedEvent.getPath());
        System.out.println("EventType = "+watchedEvent.getType().name());
        System.out.println("事件触发了....end");
    }

    public WatcherExample(Watcher watcher){
        try {
            zk = new ZooKeeper(Constants.HOST_PORT,100000,watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testWatchedDisabled(){
        MyWatcher myWatcher1 = new MyWatcher();
        try {
            zk.getData("/node2",myWatcher1,null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyWatcher myWatcher = new MyWatcher();
        WatcherExample watcherExample = new WatcherExample(myWatcher);
        watcherExample.testWatchedDisabled();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
