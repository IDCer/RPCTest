package client;


import api.config.RPCConfig;
import api.interfaces.RPCAnnotation;
import api.service.RemoteService;

public class ClientAction {
    public static void main(String [] args) {
        // 1.创建一个客户端
        RPCClient rpcClient = new RPCClient();

        // 2.首先向注册中心询问服务提供者的ip地址
        String serviceName = RemoteService.class.getAnnotation(RPCAnnotation.class).value().getName();
        String serviceAddress = rpcClient.queryService(serviceName, RPCConfig.registryAddress);

//        System.out.println("serviceAddress:" + serviceAddress);

        if (serviceAddress == null) {
            System.out.println("该服务不存在");
        } else {
            // 3.通过这些服务提供者的ip地址创建代理对象并使用
            RemoteService remoteService = rpcClient.getProxy(RemoteService.class, serviceAddress);

            // 4.输入服务参数,并得到结果
            String result = remoteService.sendMessage("hello guys");

            // 5.输出结果
            System.out.println(result);
        }
    }
}
