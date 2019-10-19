package server;

import api.config.RPCConfig;
import api.interfaces.RPCAnnotation;
import api.model.RPCRegisterRequest;
import server.thread.HeartBeatThread;
import server.thread.ServerThread;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RPCServer {
    /**
     * 用于记录接口与实现类之间的关系,便于客户端的查询
     */
    private static final ConcurrentHashMap<String, Object> serviceMap = new ConcurrentHashMap<String, Object>();

    // 服务地址
    private String serviceAddress;

    // 构造函数
    public RPCServer(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public static Map<String, Object> getServiceMap() {
        return serviceMap;
    }

    public String getAddressService() {
        return serviceAddress;
    }

    // 开启服务器
    public void start() {
        // 用于获取开启服务提供端的端口号
        int port = Integer.parseInt(serviceAddress.split(":")[1]);

        try (ServerSocket serverSocket = new ServerSocket(port)){
            //定时发送心跳包
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(new HeartBeatThread(serviceAddress),0, RPCConfig.sendIntervalTime, TimeUnit.SECONDS);

            while (true) {
                // 阻塞监听线程
                System.out.println("等待客户端的请求...");
                Socket socket = serverSocket.accept();
                System.out.println("收到一个客户端请求:{" + socket.getLocalAddress() + ":" + socket.getLocalPort() + "}");
                // 放入线程去执行
                new Thread(new ServerThread(socket, serviceMap)).start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务器向注册中心注册服务,可以是多个服务一起注册
     */
    public void bindService(String registryAddress, Object... services) {
        System.out.println("服务器向注册中心发送服务注册请求...");
        for (Object service : services) {
            // 获取服务的接口类对象
            RPCAnnotation rpcAnnotation = service.getClass().getAnnotation(RPCAnnotation.class);

            // 如果为空则跳过注册
            if (rpcAnnotation == null) {
                continue;
            }

            // 获取服务名称
            String serviceName = rpcAnnotation.value().getName();

            bind(registryAddress, new RPCRegisterRequest(serviceName, serviceAddress));

            // 加入{接口:实现类},用于查询客户端的请求
            serviceMap.put(serviceName, service);
        }
    }

    public void bind(String registryAddress, RPCRegisterRequest rpcRegisterRequest) {
        // 获取注册中心的ip地址和端口号
        String host = registryAddress.split(":")[0];
        int port = Integer.parseInt(registryAddress.split(":")[1]);

        Socket socket = null;
        try {
            socket = new Socket(host, port);
            // 获取输出流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 将注册请求传输给注册中心
            objectOutputStream.writeObject(rpcRegisterRequest);
            objectOutputStream.flush();
            // 关闭流
            objectOutputStream.close();
            socket.close();
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
    }
}
