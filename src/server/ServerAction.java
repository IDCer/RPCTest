package server;

import api.config.RPCConfig;
import server.impl.RemoteServiceImp;

public class ServerAction {
    public static void main(String[] args) {
        // 创建一个服务器
        RPCServer rpcServer = new RPCServer(RPCConfig.serverAddress);

        // 服务器向注册中心注册服务
        rpcServer.bindService(RPCConfig.registryAddress, new RemoteServiceImp());

        // 启动服务器
        rpcServer.start();
    }
}
