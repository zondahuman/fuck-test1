package com.fuck.test.basic.cache.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DistributedLock {

    // zk客户端连接
    private ZooKeeper zkClient;

    // 连接成功等待
    private CountDownLatch connectLatch = new CountDownLatch(1);
    // 前一个结点（锁）
    private String waitPath;
    // 结点删除等待
    private CountDownLatch waitLatch = new CountDownLatch(1);
    // 当前创建的结点（锁）
    private String createNode;

    /**
     * 构造方法:初始化客户端连接
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws KeeperException
     */
    public DistributedLock() throws IOException, InterruptedException, KeeperException {
        //获取连接
        zkClient = new ZooKeeper(ZkConstants.connectString, ZkConstants.sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //连接成功，释放countDownLatch
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    connectLatch.countDown();
                }

                //前一个结点删除
                if (watchedEvent.getType() == Event.EventType.NodeDeleted && watchedEvent.getPath().equals(waitPath)) {
                    //解锁下一个结点
                    waitLatch.countDown();
                }
            }
        });
        //等待zk正常连接后，再往下执行
        connectLatch.await();
        //判断根节点/locks是否存在
        Stat exists = zkClient.exists("/locks", false);
        if (exists == null) {
            //创建根节点 -- 持久结点
            zkClient.create("/locks", "locks".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    /**
     * 加锁
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void zkLock() throws InterruptedException, KeeperException {
        //创建对应的临时带序号结点
        createNode = zkClient.create("/locks/" + "seq-", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        //判断创建节点是否是序号最小的结点
        List<String> children = zkClient.getChildren("/locks", false);
        if (children.size() == 1) {
            return;
        } else {
            //排序结点以得到当前创建结点的序号（等待锁的序位）
            Collections.sort(children);
            //获取生成的临时结点序号
            String thisNode = createNode.substring("/locks/".length());
            //获得排序
            int index = children.indexOf(thisNode);

            if (index == -1) {
                System.out.println("数据异常");
            } else if (index == 0) {
                //最小序号结点，直接获取锁
                return;
            } else {
                //监听序号前一个结点
                waitPath = "/locks/" + children.get(index - 1);
                //true代表使用创建zkClient时初始化的监听器
                zkClient.getData(waitPath, true, null);
                waitLatch.await();
            }
        }

    }

    /**
     * 解锁
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void zkUnLock() throws InterruptedException, KeeperException {
        //删除临时带序号结点
        zkClient.delete(createNode, -1);
    }



}

