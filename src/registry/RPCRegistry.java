package registry;

import api.config.RPCConfig;
import registry.thread.ClearMemoryThread;
import registry.thread.RegistryThread;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RPCRegistry {
    /**
     * Map结构,用于存储服务器注册的服务节点的信息<服务名, 服务器IP地址>
     */
    private static final HashMap<String, String> serviceMap = new HashMap<String, String>();

    /**
     * 注册中心开启SocketServer的端口
     */
    private String address;

    /**
     * 注册中心的运行状态
     */
    private boolean running;

    /**
     * Map表结构,用于记录服务器心跳时间<服务器ip地址, 最近存活时间>
     */
    public static HashMap<String, Long> heartTable = new HashMap<String, Long>();

    public RPCRegistry(String address) {
        this.address = address;
        this.running = true;
    }

    /**
     * 注册中心开始启动
     */
    public void start() {
        // 开启SocketServer的端口,主机地址会自动调用本地地址
        int port = Integer.parseInt(address.split(":")[1]);

        // 创建注册中心服务
        try (ServerSocket serverSocket = new ServerSocket(port)){

            // 启动清理无连接服务器节点的线程
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(new ClearMemoryThread(),0, RPCConfig.clearIntervalTime, TimeUnit.SECONDS);

            while(running) {
                // 阻塞监听来自客户端或者服务端的socket请求
                System.out.println("等待请求...");
                Socket socket = serverSocket.accept();
                System.out.println("接受到一个请求,来源为 -> " + socket.getLocalAddress() + ":" + socket.getLocalPort());
                // start a new thread to handle
                new Thread(new RegistryThread(socket, this)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于持久化节点
     */
    public void register(String serviceName, String serviceAddress) {
        // 开始注册服务
        System.out.println("注册中心开始注册服务...");
        serviceMap.put(serviceName, serviceAddress);

        System.out.println("成功注册的服务列表:" + serviceMap);

        // 还未持久化
        // ...

    }

    /**
     * 更新心跳表,只管更新,不管其是否超时等等
     */
    public void update(String serverAddress, long updateTime) {
        heartTable.put(serverAddress, updateTime);
    }

    /**
     * 清理失联服务所存储的节点
     */
    public void clear(String serviceAddress) {
        List<String> willDelete = new ArrayList<>();
        for(Map.Entry<String, String> entry : RPCRegistry.serviceMap.entrySet()){
            if (entry.getValue() == serviceAddress) {
                willDelete.add(entry.getKey());
            }
        }
        // 删除
        for (String serviceName : willDelete) {
            RPCRegistry.serviceMap.remove(serviceName);
        }

        System.out.println("serviceMap:" + serviceMap);

    }

    /**
     * 注册中心进行查询操作
     */
    public String query(String serviceName) {
        if (serviceMap.containsKey(serviceName)) {
            return serviceMap.get(serviceName);
        } else {
            System.out.println("无法寻找到该服务:{" + serviceName + "}");
        }
        return null;
    }


}
