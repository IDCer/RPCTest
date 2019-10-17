package registry.thread;

import api.interfaces.RPCSignal;
import api.model.RPCHeartBeatPacket;
import api.model.RPCRequest;
import registry.RPCRegistry;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

            System.out.println("信号类型: " + signalKind);

            // 如果信号类型为HEART,服务器心跳
            if (signalKind.equals("HEART")) {
                // 接收到服务器心跳时,实时更新heartTable

                // 强转数据类型
                RPCHeartBeatPacket rpcHeartBeatPacket = (RPCHeartBeatPacket) rpcSignal;

                // 更新
                rpcRegistry.update(rpcHeartBeatPacket.getServiceAddress(), rpcHeartBeatPacket.getTime());

            } else if (signalKind.equals("REGISTER")) { // 如果信号类型为REGISTER,服务器第一次注册

            } else if (signalKind.equals("QUERY")) { // 如果信号类型为QUERY,即客户端查询服务

            }



//            Object result = invoke(rpcRequest);
//
//            // 返回客户端
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
//            objectOutputStream.writeObject(result);
//
//            // 刷新缓冲区
//            objectOutputStream.flush();
//
//            // 关闭流
//            objectOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 检查输入流是否关闭
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
