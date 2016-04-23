package com.lewis.zookeeper.operation;

import com.lewis.zookeeper.StaticProperty;
import com.lewis.zookeeper.ZKUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import java.io.IOException;
import java.util.List;

public class ZkOperation {
    private static ZooKeeper zk  = null;

    public ZkOperation(String connectString,int timeout){
        try {
            zk = new ZooKeeper(connectString,timeout,null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (zk != null) {
                try {
                    zk.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    //同步创建节点
    public String testCreateNodeSyn(String path, byte[] data, List<ACL> acls, CreateMode createMode){
        String result = "";
        try {
             result = zk.create(path, data, acls, createMode);
            System.out.println("创建节点【"+result+"】成功！");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Stat testSetDataSyn(String path,byte[] data,int version){
        Stat stat = null;
        try {
            stat = zk.setData(path, data, version);
            System.out.println("setDataSyn Stat = "+stat.toString());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stat;
    }

    public void testSetDataAsyn(String path, byte[] data, int version, AsyncCallback.StatCallback callback,Object ctx){
        zk.setData(path,data,version,callback,ctx);
    }

    public void testCreateNodeAsyn(String path, byte[] data, List<ACL> acls, CreateMode createMode, AsyncCallback.StringCallback callback,Object ctx){
        zk.create(path,data,acls,createMode,callback,ctx);
        System.out.println("invoke method testCreateNodeAsyn");
    }

    public void testDeleteNodeSyn(String path,int version){
        try {
            zk.delete(path,version);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void testDeleteNodeAsyn(String path, int version, AsyncCallback.VoidCallback callback,Object ctx){
        System.out.println("invoke delete before");
        zk.delete(path,version,callback,ctx);
        System.out.println("invoke delete after");
    }





    public static void main(String[] args) throws InterruptedException, KeeperException {
        final ZkOperation zkOperation = new ZkOperation(StaticProperty.connectString,StaticProperty.timeout);
        //确保ZK客户端连上ZK服务器之后，再进行下面的操作
        ZKUtils.confirmConnected(zk);
        final String path ="/acl/node4";
        AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {
            public void processResult(int rc, String path, Object ctx, String name) {
                System.out.println("callback :rc ="+rc +",path ="+path+", ctx="+ctx+", name="+name);
            }
        };
        //异步创建节点
        zkOperation.testCreateNodeAsyn(path,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT,callback,"子节点创建成功！！");
        //程序执行到这，由于上面是异步创建节点有可能此时节点还没有创建成功，所以节点创建成功之前不退出主程序，不进行修改数据
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    while (true) {
                        Stat stat = zk.exists(path, null);
                        if (stat == null) {
                            Thread.sleep(10);
                        }else{
                            //只有节点创建成功之后，才进行节点的数据修改操作
                            System.out.println(stat.toString());
                            int version = stat.getVersion();
                            zkOperation.testSetDataSyn(path,"hahaha".getBytes(),version);
                            System.out.println("main exit");
                            break;
                        }
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
