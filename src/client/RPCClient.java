package client;

import api.model.RPCServiceQueryRequest;
import client.proxy.RPCClientProxy;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 客户端返回服务的代理实现对象
 */
public class RPCClient {

    /**
     * 获取服务的实现类对象代理
     */
    public <T> T getProxy(final Class<T> interfaceClass, final String serviceAddress)  {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new RPCClientProxy(serviceAddress));
    }

    /**
     * 客户端向注册中心访问服务提供者的地址
     */
//    public String queryService(final String serviceName, final String registryAddress) {
    public ArrayList<String> queryService(final String serviceName, final String registryAddress) {
        RPCServiceQueryRequest rpcServiceQueryRequest = new RPCServiceQueryRequest(serviceName);
        return query(registryAddress, rpcServiceQueryRequest);
    }

//    public String query(String registryAddress, RPCServiceQueryRequest rpcServiceQueryRequest) {
    public ArrayList<String> query(String registryAddress, RPCServiceQueryRequest rpcServiceQueryRequest) {
        // 获取注册中心ip地址和端口号
        String [] arr = registryAddress.split(":");
        String host = arr[0];
        int port = Integer.parseInt(arr[1]);

        // 新建一个会话
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            // 获取注册中心会话的输出流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 将客户端请求传输给注册中心
            objectOutputStream.writeObject(rpcServiceQueryRequest);
            objectOutputStream.flush();

            // 从注册中心返回查询结果
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            String result = (String) objectInputStream.readObject();
            ArrayList<String> serviceListResult = (ArrayList<String>) objectInputStream.readObject();

            // 关闭输出流和会话
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();

            // 返回结果
//            return result;
            return serviceListResult;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 随机算法实现
     */
    public String RANDOM(ArrayList<String> list) {
        int len = list.size();
        return list.get(new Random().nextInt(len));
    }

    /**
     * 最近最少使用算法实现
     */
    public String LRU() {
        return "";
    }

}
