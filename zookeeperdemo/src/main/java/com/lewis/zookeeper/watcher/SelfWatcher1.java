package com.lewis.zookeeper.watcher;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Created by zhangminghua on 2016/4/18.
 */
public class SelfWatcher1  {
    ZooKeeper zk = null;
    private String path ="/root";
    private Watcher getWatcher(final String msg){
        return new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println(msg +"\t"+event.toString());
            }
        };
    }

    SelfWatcher1(String address){
        try {
            zk = new ZooKeeper(address,3000,null);
            Thread.sleep(50);
            zk.create(path,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
            zk = null;
        }
    }

    void setWatcher(){
        try {
            Stat s = zk.exists(path, getWatcher("exists"));
            if (s != null) {
                byte[] getDatas = zk.getData(path, getWatcher("getData"), s);
                System.out.println("nowGetData:"+new String(getDatas));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void triggerWatcher(){
        try {
            Stat s = zk.exists(path, getWatcher("trigger"));
            zk.setData(path,"a".getBytes(),s.getVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void disconnect(){
        try {
            if (zk != null) {
                zk.close();
            }
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        SelfWatcher1 instance = new SelfWatcher1("localhost:2181");
        instance.setWatcher();
        System.out.println("trigger one :");
        instance.triggerWatcher();
        System.out.println("trigger two :");
        instance.triggerWatcher();
        Thread.sleep(3000000);
        instance.disconnect();
    }

}
