package registry.thread;

import api.config.RPCConfig;
import api.interfaces.RPCSignal;
import api.model.RPCHeartBeatPacket;
import api.model.RPCRegisterRequest;
import api.model.RPCServiceQueryRequest;
import registry.RPCRegistry;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class RegistryThread implements Runnable  {

    // 客户端会话对象
    private Socket client;

    // 持有一个注册中心对象
    private RPCRegistry rpcRegistry;


    public RegistryThread(Socket client, RPCRegistry rpcRegistry) {
        this.client = client;
        this.rpcRegistry = rpcRegistry;
    }

    @Override
    public void run() {
        // 接受客户端信息流
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(client.getInputStream());

            // 反序列化,读取信号的类型
            RPCSignal rpcSignal = (RPCSignal) objectInputStream.readObject();

            // 读取信号类型
            String signalKind = rpcSignal.getKind();

//            System.out.println("信号类型: " + signalKind);

            // 如果信号类型为HEART,服务器心跳
            if (signalKind.equals("HEART")) {
                // 接收到服务器心跳时,实时更新heartTable
                // 强转数据类型
                RPCHeartBeatPacket rpcHeartBeatPacket = (RPCHeartBeatPacket) rpcSignal;

                System.out.println(RPCConfig.registryHead + "接收到来自{" + rpcHeartBeatPacket.getServiceAddress() + "}的心跳...");
                // 更新
                rpcRegistry.update(rpcHeartBeatPacket);

            } else if (signalKind.equals("REGISTER")) { // 如果信号类型为REGISTER,服务器第一次注册
                // 强转类型
                RPCRegisterRequest rpcRegisterRequest = (RPCRegisterRequest) rpcSignal;
                System.out.println(RPCConfig.registryHead + "接收到来自{" + rpcRegisterRequest.getServiceAddress() +
                        "}的服务注册请求{"+ rpcRegisterRequest.getServiceName() +"}");

                rpcRegistry.register(rpcRegisterRequest.getServiceName(), rpcRegisterRequest.getServiceAddress());

            } else if (signalKind.equals("QUERY")) { // 如果信号类型为QUERY,即客户端查询服务

                RPCServiceQueryRequest rpcServiceQueryRequest = (RPCServiceQueryRequest) rpcSignal;
                // 注册中心进行查询操作,返回查询地址
//                String serviceAddress = rpcRegistry.query(rpcServiceQueryRequest.getServiceName());
                System.out.println(RPCConfig.registryHead + "接收到来自客户端的查询服务{" + rpcServiceQueryRequest.getServiceName() +"}");
                ArrayList<String> serviceAddress = rpcRegistry.queryServiceList(rpcServiceQueryRequest.getServiceName());

                // 发送结果给客户端
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(serviceAddress);
                objectOutputStream.flush();

                // 关闭流
                objectOutputStream.close();
            }
            // 关闭流
            objectInputStream.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 检查输入流是否关闭
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
