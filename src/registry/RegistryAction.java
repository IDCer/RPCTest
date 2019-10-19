package registry;

import api.config.RPCConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class RegistryAction {
    public static void main(String [] args) {
        // 创建一个注册中心
        RPCRegistry rpcRegistry = new RPCRegistry(RPCConfig.registryAddress);

        // 注册中心启动,监听请求线程
        rpcRegistry.start();
    }
}
