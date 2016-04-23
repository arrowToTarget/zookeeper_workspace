package com.lewis.zookeeper.watcher;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangminghua on 2016/4/18.
 */
public class DigestDemo {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(DigestAuthenticationProvider.generateDigest("admin:222222"));
    }
}
