package server;

import api.interfaces.RPCRegistryCenter;
import server.impl.MessageServiceImp;
import server.impl.RPCRegistryCenterImpl;

public class ServerAction {
    public static void main(String[] args) {
        // 创建一个注册中心
//        RPCRegistryCenter rpcRegistryCenter = new RPCRegistryCenterImpl();

        // 服务器运行ip地址
        String address = "127.0.0.1:11234";
//        RPCServer rpcServer = new RPCServer(rpcRegistryCenter, address);
        RPCServer rpcServer = new RPCServer(address);

        // 绑定注册服务服务
        rpcServer.bind(new MessageServiceImp());

        // 启动服务器
        rpcServer.start();

    }
}
