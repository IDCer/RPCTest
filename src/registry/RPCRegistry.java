package registry;

import api.interfaces.ClearMemoryConfig;
import registry.thread.ClearMemoryThread;
import registry.thread.RegistryThread;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RPCRegistry {
    /**
     * a Map struct, use to store the node's info
     */
    private static final HashMap<String, Object> serviceMap = new HashMap<String, Object>();

    /**
     * service port
     */
    private String address;

    /**
     * registry running state
     */
    private boolean running;

    /**
     * heart record table
     */
    public static HashMap<String, Long> heartTable = new HashMap<String, Long>();

    public RPCRegistry(String address) {
        this.address = address;
        this.running = true;
    }

    /**
     * start the registry server
     */
    public void start() {
        // SocketServer's port, the localhost will auto set naive
        int port = Integer.parseInt(address.split(":")[1]);

        // 创建注册中心服务
        try (ServerSocket serverSocket = new ServerSocket(port)){


            // 启动清理无连接服务器节点的线程
            // ...
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(new ClearMemoryThread(),0, ClearMemoryConfig.clearIntervalTime, TimeUnit.SECONDS);

            while(running) {
                // stop to listen the server or client
                System.out.println("wait for a client...");
                Socket socket = serverSocket.accept();
                System.out.println("receive a client request -> " + socket.getLocalAddress() + ":" + socket.getLocalPort());
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
    public void clear() {

    }


}
