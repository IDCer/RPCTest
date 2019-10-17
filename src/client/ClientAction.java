package client;

import api.interfaces.RPCRegistryCenterConfig;
import api.interfaces.RPCServiceDiscovery;
import api.service.MessageService;
import server.impl.RPCServiceDiscoveryImpl;

public class ClientAction {
    public static void main(String [] args) {
//		// 从客户端处得到一个关于该接口的代理对象
//		MessageService messageService = new RPCClient().getProxy(MessageService.class, "127.0.0.1", 11234);
//
//		// 输入服务参数,并得到结果
//		String result = messageService.sendMessage("hello guys");
//
//		// 输出结果
//		System.out.println(result);

        // -----------------分割线-------------------

//        // 新建一个服务发现中心
//        RPCServiceDiscovery rpcServiceDiscovery = new RPCServiceDiscoveryImpl(RPCRegistryCenterConfig.CONNECTING_STR);
//
//        // 新建客户端
//        RPCClient rpcClient = new RPCClient(rpcServiceDiscovery);
//
//        // 输入服务class对象,得到服务句柄,从远程服务器
//        MessageService messageService = rpcClient.getProxy(MessageService.class);
//
//        // 输出结果
//        System.out.println(messageService.sendMessage("hi guys!"));

        // -----------------无服务发现中心版本--------------
        // 从客户端处得到一个关于该接口的代理对象
        MessageService messageService = new RPCClient().getProxy(MessageService.class, "127.0.0.1", 11234);

        // 输入服务参数,并得到结果
        String result = messageService.sendMessage("hello guys");

        // 输出结果
        System.out.println(result);

    }
}
