package com.lewis.zookeeper.watcher;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Created by zhangminghua on 2016/4/18.
 */
public class SelfWatcher implements Watcher{

    private String path ="/node1";
    private ZooKeeper zk = null;
    public void process(WatchedEvent event) {
        System.out.println(event.toString());
    }

    SelfWatcher(String address){
        try {
            zk = new ZooKeeper(address,3000,this);
            zk.create(path,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
            zk = null;
        }
    }

    void setWatcher(){
        try {
            Stat stat = zk.exists(path, true);
            if (stat != null) {
                zk.getData(path,false,stat);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void triggerWatcher(){
        try {
            Stat s = zk.exists(path, false);
            zk.setData(path,"a".getBytes(),s.getVersion());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void disconnect(){
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SelfWatcher instance = new SelfWatcher("localhost:2181");
        instance.setWatcher();
        instance.triggerWatcher();
        instance.disconnect();
    }
}
