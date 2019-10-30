package client;


import api.config.RPCConfig;
import api.interfaces.RPCAnnotation;
import api.service.RemoteReckonService;
import api.service.RemoteService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientAction {
    public static void main(String [] args) {
//        // 1.创建一个客户端
//        RPCClient rpcClient = new RPCClient();
//        // 2.首先向注册中心询问服务提供者的ip地址列表
//        String serviceName = RemoteService.class.getAnnotation(RPCAnnotation.class).value().getName();
//        ArrayList<String> serviceAddress = rpcClient.queryService(serviceName, RPCConfig.registryAddress);
//
//        System.out.println("serviceAddress list:" + serviceAddress);
//        String bestAddress = null;
//        if (serviceAddress != null && serviceAddress.size() != 0) {
//            // 根据负载均衡算法:最近最少使用(LRU)去选择服务器.
////          String bestAddress = rpcClient.LRU(serviceAddress);
//            // 随机算法
//            bestAddress = rpcClient.RANDOM(serviceAddress);
//            // 3.通过这些服务提供者的ip地址创建代理对象并使用
//            RemoteService remoteService = rpcClient.getProxy(RemoteService.class, bestAddress);
//
//            // 4.输入服务参数,并得到结果
//            String result = remoteService.reverseString("hello guys");
//
//            // 5.输出结果
//            System.out.println(result);
//        } else {
//            System.out.println("服务器列表为空");
//        }
//        if (serviceAddress == null) {
//            System.out.println("服务{" + serviceName + "}不存在");
//        } else {
//            // 3.通过这些服务提供者的ip地址创建代理对象并使用
//            RemoteService remoteService = rpcClient.getProxy(RemoteService.class, serviceAddress);
//
//            // 4.输入服务参数,并得到结果
//            String result = remoteService.reverseString("hello guys");
//
//            // 5.输出结果
//            System.out.println(result);
//        }


        // 1.创建一个客户端
        RPCClient rpcClient = new RPCClient();

        // 2.查询服务
        System.out.println(RPCConfig.clientHead + "客户端查询服务...");
        String reckon = RemoteReckonService.class.getAnnotation(RPCAnnotation.class).value().getName();

        // 3.向注册中心发送请求
        ArrayList<String> reckonAddress = rpcClient.queryService(reckon, RPCConfig.registryAddress);

        String bAddress = null;
        if (reckonAddress != null && reckonAddress.size() != 0) {
            System.out.println(RPCConfig.clientHead + "可以使用该服务的服务器有:" + reckonAddress);
            // 随机算法
            bAddress = rpcClient.RANDOM(reckonAddress);
            RemoteReckonService remoteService = rpcClient.getProxy(RemoteReckonService.class, bAddress);

            // 4.输入服务参数,并得到结果
            int result = remoteService.sum(1, 2);

            // 5.输出结果
            System.out.println(RPCConfig.clientHead + "运算结果为:" + result);
        } else {
            System.out.println(RPCConfig.clientHead + "可用服务器列表为空...");
        }





//        if (serviceAddress == null) {
//            System.out.println("服务{" + serviceName + "}不存在");
//        } else {
//            // 3.通过这些服务提供者的ip地址创建代理对象并使用
//            RemoteService remoteService = rpcClient.getProxy(RemoteService.class, serviceAddress);
//
//            // 4.输入服务参数,并得到结果
//            String result = remoteService.reverseString("hello guys");
//
//            // 5.输出结果
//            System.out.println(result);
//        }

    }
}
