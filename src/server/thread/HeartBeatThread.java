package server.thread;

import api.interfaces.RPCRegistryCenterConfig;
import api.model.RPCHeartBeatPacket;
import server.RPCServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 该线程用于服务端对注册中心进行心跳包的发送
 */
public class HeartBeatThread implements Runnable {

    // 持有一个RPCServer对象
    private RPCServer rpcServer;

    // 构造函数
    public HeartBeatThread(RPCServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    @Override
    public void run() {
        // 发送心跳包的时间
        long currentTime = System.currentTimeMillis();

        // 构建心跳包
        RPCHeartBeatPacket rpcHeartBeatPacket = new RPCHeartBeatPacket(currentTime, rpcServer.getServiceMap(), rpcServer.getAddressService());

        // 发送心跳包
        System.out.println("发送心跳包...");
        sendMessage(rpcHeartBeatPacket);
        System.out.println("成功发送心跳包...");
    }

    public void sendMessage(Object obj) {
        String [] ipAndPort = RPCRegistryCenterConfig.registryAddress.split(":");
        try {
            // 构建与注册中心的socket连接
            Socket socket = new Socket(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
            // 获取输出流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 将对象传输给注册中心
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
