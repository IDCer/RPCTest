package server.impl;


import api.interfaces.RPCServiceDiscovery;

import java.util.List;

public class RPCServiceDiscoveryImpl implements RPCServiceDiscovery {

    // 当前服务之下所有的协议地址
    private List<String> repos;

    // zookeeper的地址
    private String zookeeperAddress;

//    // zookeeper调用组件
//    private CuratorFramework curatorFramework;
//
//    // 构造函数
    public RPCServiceDiscoveryImpl(String zookeeperAddress) {
//        this.zookeeperAddress = zookeeperAddress;
//        this.curatorFramework = CuratorFrameworkFactory.builder()
//        .connectString(RPCRegistryCenterConfig.CONNECTING_STR)
//        .sessionTimeoutMs(RPCRegistryCenterConfig.SESSION_TIMEOUT)
//        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
//
//        curatorFramework.start();
    }
//
//    /**
//     * 监听节点变化,更新repos
//     */
//    public void registerWatcher(String path) {
//        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);
//
//        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
//            @Override
//            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
//                repos = curatorFramework.getChildren().forPath(path);
//            }
//        };
//        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
//        try {
//            pathChildrenCache.start();
//        } catch (Exception e) {
//            throw new RuntimeException("监听节点发生异常变化", e);
//        }
//
//    }

    @Override
    public String discover(String serviceName) {
        // 获取serviceName节点下的所有协议地址
//        String nodePath = RPCRegistryCenterConfig.NAMESPACE + "/" + serviceName;
//
//        try {
//            repos = curatorFramework.getChildren().forPath(nodePath);
//        } catch (Exception e) {
//            throw new RuntimeException("服务发现子节点产生异常", e);
//        }
//
//        // 动态发现服务节点变化,需要注册监听
//        registerWatcher(nodePath);
//
//        // 随机负载
//        RPCLoadBalance loadBalance = new RPCRandomLoadBalance();
//
//        return loadBalance.selectHost(repos);
        return null;
    }

}
