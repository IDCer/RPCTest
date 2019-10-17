package registry;

import api.interfaces.RPCRegistryCenterConfig;

public class RegistryAction {
    public static void main(String [] args) {
        // 创建一个服务中心
        RPCRegistry rpcRegistry = new RPCRegistry(RPCRegistryCenterConfig.registryAddress);

        // 启动创建中心的监听机制
        rpcRegistry.start();
    }
}
