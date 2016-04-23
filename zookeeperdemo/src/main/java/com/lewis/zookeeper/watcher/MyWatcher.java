package com.lewis.zookeeper.watcher;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created by zhangminghua on 2016/4/12.
 */
public class MyWatcher implements Watcher{

    /*private static final MyWatcher watcher = new MyWatcher();
    private static ZooKeeper zk;

    static {
        try {
            zk = new ZooKeeper(Constants.HOST_PORT, 100000, watcher);
        } catch (IOException e) {

        }
    }

    public static ZooKeeper getZk(){
        return zk;
    }*/

    public void process(WatchedEvent watchedEvent) {
        System.out.println("Watcher = "+this.getClass().getName());
        System.out.println("Path = "+watchedEvent.getPath());
        System.out.println("EventType = "+watchedEvent.getType().name());

    }

    public static void main(String[] args) {
        try {
            ZooKeeper zk = new ZooKeeper(Constants.HOST_PORT,100000,new MyWatcher());
            zk.create(Constants.WATCH_PATH,"data1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            Stat exists = zk.exists(Constants.WATCH_PATH, false);
            System.out.println("Stat = "+exists.toString());
            byte[] data = zk.getData(Constants.WATCH_PATH, true, exists);
            System.out.println("data ="+new String(data));
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
