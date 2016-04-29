package com.lewis.zookeeper.curator.demo1;

import com.lewis.zookeeper.watcher.Constants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * Created by zhangminghua on 2016/4/29.
 */
public class CuratorClientTest {

    private CuratorFramework client = null;

    public CuratorClientTest() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(Constants.HOST_POST_VM_2181)
                .sessionTimeoutMs(1000).retryPolicy(retryPolicy).namespace("base").build();
        client.start();
    }

    public void closeClient(){
        if (client != null) {
            client.close();
        }
    }

    public void createNode(String path,byte[] data) throws Exception {
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path,data);
    }

    public static void main(String[] args) {
       CuratorClientTest test = new CuratorClientTest();
        try {
            test.createNode("/node1/node11","node11".getBytes());
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            test.closeClient();
        }
        System.out.println("heheh");
    }
}
