package registry;

import api.config.RPCConfig;

public class RegistryAction {
    public static void main(String [] args) {
        // 创建一个注册中心
        RPCRegistry rpcRegistry = new RPCRegistry(RPCConfig.registryAddress);

        // 注册中心启动,监听请求线程
        rpcRegistry.start();

        // 注册中心还要考虑一个注册中心重启的问题,也就是节点的持久化
    }
}
