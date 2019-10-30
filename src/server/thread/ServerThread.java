package server.thread;


import api.config.RPCConfig;
import api.model.RPCRequest;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServerThread implements Runnable {

    // 客户端会话对象
    private Socket client;

    // 用于记录服务和服务地址的映射表
    private ConcurrentHashMap<String, Object> handlerMap;

    // 构造函数
    public ServerThread(Socket client, ConcurrentHashMap<String, Object> handlerMap) {
        this.client = client;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        // 接受客户端信息流
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(client.getInputStream());

            // 反序列化
            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            Object result = invoke(rpcRequest);
            System.out.println(RPCConfig.serverHead + "收到调用{" + rpcRequest.getClassName() + "}服务的请求...");

            // 返回客户端
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject(result);

            // 刷新缓冲区
            objectOutputStream.flush();

            // 关闭流
            objectOutputStream.close();

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

    private Object invoke(RPCRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 获取客户端传来的参数
        Object [] parameters = rpcRequest.getParameters();
        Class [] parameterTypes = new Class[parameters.length];
        // 设置参数类型
        for (int i = 0, length = parameters.length; i < length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }

        // 获取服务的类对象
        Object service = handlerMap.get(rpcRequest.getClassName());

//        System.out.println("service:" + service);
//        System.out.println("rpcRequest.getMethodName():" + rpcRequest.getMethodName());
//        System.out.println("service.getClass():" + service.getClass());
//        System.out.println("getMethod(rpcRequest.getMethodName(), parameterTypes):" + service.getClass().getMethod(rpcRequest.getMethodName(), parameterTypes));

        // 获取这个服务类的方法
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), parameterTypes);

        // 返回方法调用结果
        return method.invoke(service, parameters);

    }
}
