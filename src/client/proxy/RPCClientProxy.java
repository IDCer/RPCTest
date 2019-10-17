package client.proxy;

import api.interfaces.RPCServiceDiscovery;
import api.model.RPCRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class RPCClientProxy implements InvocationHandler {

    // 主机地址
    private String host;

    // 端口号
    private int port;

    // 服务发现中心
    private RPCServiceDiscovery rpcServiceDiscovery;

    // 构造函数
    public RPCClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RPCClientProxy(RPCServiceDiscovery rpcServiceDiscovery) {
        this.rpcServiceDiscovery = rpcServiceDiscovery;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 新建一个传输信息对象
        RPCRequest rpcRequest = new RPCRequest();
        // 类名
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        // 方法名
        rpcRequest.setMethodName(method.getName());
        // 参数名
        rpcRequest.setParameters(args);

        // 新建一个会话连接
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            System.out.println("socket:" + socket);

            // 获取会话socket的输出流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 将对象写入
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();

            // 获取会话socket的输出流,接受服务器返回的数据
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 返回服务器的结果
            Object result = objectInputStream.readObject();

            // 关闭流
            objectInputStream.close();
            objectOutputStream.close();

            // 返回结果
            return result;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        // 新建一个传输信息对象
//        RPCRequest rpcRequest = new RPCRequest();
//        // 类名
//        rpcRequest.setClassName(method.getDeclaringClass().getName());
//        // 方法名
//        rpcRequest.setMethodName(method.getName());
//        // 参数名
//        rpcRequest.setParameters(args);
//
//        // 从zookeeper中获取地址
//        String discover = rpcServiceDiscovery.discover(rpcRequest.getClassName());
//
//        System.out.println("discover :" + discover);
//
//        // 分解地址
//        String [] hostport = discover.split(":");
//
//        // 新建一个会话连接
//        Socket socket = null;
//        try {
//            socket = new Socket(hostport[0], Integer.parseInt(hostport[1]));
//
//            // 获取会话socket的输出流
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            // 将对象写入
//            objectOutputStream.writeObject(rpcRequest);
//            objectOutputStream.flush();
//
//            // 获取会话socket的输出流,接受服务器返回的数据
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            // 返回服务器的结果
//            Object result = objectInputStream.readObject();
//
//            // 关闭流
//            objectInputStream.close();
//            objectOutputStream.close();
//
//            // 返回结果
//            return result;
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(socket != null) {
//                try {
//                    socket.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
}
