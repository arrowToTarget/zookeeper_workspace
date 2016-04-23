package com.lewis.zookeeper.watcher.demo1;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class LewisWather implements Watcher{

    private ZooKeeper zk = null ;

    public void process(WatchedEvent event) {
        System.out.println("this WatchedEvent come from LewisWatcher1:"+event.toString());
        LewisWather wather1 = new LewisWather();
        wather1.setZk(zk);
        try {
            //重新设置Watcher
            zk.getData(event.getPath(),wather1,null);
        } catch (KeeperException e) {
            e.printStackTrace();
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
