package client.proxy;

import api.model.RPCRequest;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class RPCClientProxy implements InvocationHandler {
    /**
     * 服务提供者的地址和端口
     */
    private String serviceAddress;

    public RPCClientProxy(String serviceAddress) {
        this.serviceAddress = serviceAddress;
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

        System.out.println("rpcRequest:" + rpcRequest);

        // 获取主机号和端口
        String host = serviceAddress.split(":")[0];
        int port = Integer.parseInt(serviceAddress.split(":")[1]);

        // 新建一个会话连接
        Socket socket = null;
        try {
            socket = new Socket(host, port);

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
}
