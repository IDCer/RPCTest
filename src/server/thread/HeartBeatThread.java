package server.thread;

import api.config.RPCConfig;
import api.model.RPCHeartBeatPacket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该线程用于服务端对注册中心进行心跳包的发送
 */
public class HeartBeatThread implements Runnable {

    // 服务器地址
    private String serviceAddress;

    // 服务列表
    private ConcurrentHashMap<String, ArrayList<String>> serviceMap = new ConcurrentHashMap<>();

    public HeartBeatThread(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
    public HeartBeatThread(ConcurrentHashMap<String, ArrayList<String>> serviceMap, String serviceAddress) {
        this.serviceAddress = serviceAddress;
        this.serviceMap = serviceMap;
    }

    @Override
    public void run() {
        // 发送心跳包的时间
        long currentTime = System.currentTimeMillis();

        // 取出服务器提供的服务列表
        ArrayList<String> list = serviceMap.get(serviceAddress);

        // 构建心跳包
        RPCHeartBeatPacket rpcHeartBeatPacket = new RPCHeartBeatPacket(currentTime, serviceAddress, list);

        // 发送心跳包
        sendMessage(rpcHeartBeatPacket);
    }

    /**
     * 发送心跳包的socket会话
     */
    public void sendMessage(Object obj) {
        String [] ipAndPort = RPCConfig.registryAddress.split(":");
        try {
            // 构建与注册中心的socket连接
            Socket socket = new Socket(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
            // 获取输出流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 将对象传输给注册中心
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();

            // 关闭流
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
