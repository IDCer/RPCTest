package registry;

import api.config.RPCConfig;
import api.model.RPCHeartBeatPacket;
import registry.thread.ClearMemoryThread;
import registry.thread.RegistryThread;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RPCRegistry {
    /**
     * Map结构,用于存储服务器注册的服务节点的信息<服务名, 服务器IP地址>
     */
    public static final ConcurrentHashMap<String, String> serviceMap = new ConcurrentHashMap<>();

    /**
     * Map结构,用于存储服务器注册的服务节点的信息<服务名, 服务器IP地址列表>
     */
    public static final ConcurrentHashMap<String, ArrayList<String>> serviceMapList = new ConcurrentHashMap<>();

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
    public static ConcurrentHashMap<String, Long> heartTable = new ConcurrentHashMap<>();

    public RPCRegistry(String address) {
        this.address = address;
        this.running = true;
        // 重启注册中心时会热部署,读取已经存储了的服务
//        hotStart();
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
            service.scheduleAtFixedRate(new ClearMemoryThread(this),0, RPCConfig.clearIntervalTime, TimeUnit.SECONDS);

            while(running) {
                // 阻塞监听来自客户端或者服务端的socket请求
                System.out.println("等待请求...");
                Socket socket = serverSocket.accept();
                System.out.println("接受到一个请求,开始处理...");
                // 注册中心线程进行处理
                new Thread(new RegistryThread(socket, this)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在注册中心注册服务节点
     */
    public void register(String serviceName, String serviceAddress) {
        // 开始注册服务
        System.out.println("注册中心开始注册服务...");

        // 添加链表
        ArrayList<String> temp = null;
        if (serviceMapList.get(serviceName) == null) {
            temp = new ArrayList<>();
        } else  {
            temp = serviceMapList.get(serviceName);
        }
        temp.add(serviceAddress);
        serviceMapList.put(serviceName, temp);

        System.out.println("成功注册的服务列表:" + serviceMapList);

        // 持久化服务节点
        for(Map.Entry<String, ArrayList<String>> entry : RPCRegistry.serviceMapList.entrySet()){
            for (String address : entry.getValue()) {
                System.out.println(entry.getKey() + "=" + address);
//                resistNode(entry.getKey(), address);
            }
        }

    }

    /**
     * 持久化节点
     */
    public void resistNode(String serviceName, String serviceAddress) {
        try {
            String path = RPCConfig.nodeNameSpace + serviceName + "/";
            File file = new File(path);

            // 首先创建服务的文件夹,不存在则创建
            if (!file.exists()) {
                file.mkdirs();
            }
            // 如果不存在则添加服务地址,换成合法格式
            path += (serviceAddress.split(":")[0] + "-" + serviceAddress.split(":")[1]);
            File serviceAddFile = new File(path);
            if (!serviceAddFile.exists()) {
                serviceAddFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新心跳表,只管更新,不管其是否超时等等
     */
    public void update(RPCHeartBeatPacket rpcHeartBeatPacket) {
        // 更新心跳表
        String serverAddress = rpcHeartBeatPacket.getServiceAddress();
        long updateTime = rpcHeartBeatPacket.getTime();
        heartTable.put(serverAddress, updateTime);

        // 接收心跳不仅仅要更新heartTable,还需要更新serviceMapList<服务名, 服务器IP地址列表>
        ArrayList<String> sl = rpcHeartBeatPacket.getServiceList();
        for (String sn : sl) {
            ArrayList<String> temp = serviceMapList.get(sn);
            // 如果列表还未存在
            if (temp == null || temp.isEmpty()) {
                temp = new ArrayList<>();
            }
            if (!temp.contains(serverAddress)){
                temp.add(serverAddress);
               serviceMapList.put(sn, temp);
             }
        }
        System.out.println(serviceMapList);

    }

    /**
     * 清理失联服务所存储的节点
     */
    public void clear(String serviceAddress) {
//        System.out.println("将要删除的地址:" + serviceAddress);
        List<String> willDelete = new ArrayList<>();
        for(Map.Entry<String, ArrayList<String>> entry : RPCRegistry.serviceMapList.entrySet()){
            ArrayList<String> temp = entry.getValue();
            for (String address : temp) {
                if (address.equals(serviceAddress)) {
                    willDelete.add(entry.getKey());
                    break;
                }
            }
        }
        // 删除
        for (String serviceName : willDelete) {
            System.out.println("serviceName:" + serviceName + ",serviceAddress" + serviceAddress);
            // 删除持久化
            deleteLocalNode(serviceName, serviceAddress);

            // 删除注册中心存储的节点地址
//            RPCRegistry.serviceMap.remove(serviceName);
//            serviceMap.remove(serviceName);
            removeServiceAddress(serviceName, serviceAddress);
        }
        System.out.println("清理无服务节点后的serviceMapList:" + serviceMapList);
    }

    /**
     * 删除持久化数据服务节点
     */
    public void deleteLocalNode(String serviceName, String serviceAddress) {
        // 首先先将地址文件删除
        String newServiceAddress = serviceAddress.split(":")[0] + "-" + serviceAddress.split(":")[1];
        String fileName = RPCConfig.nodeNameSpace + serviceName + "/" + newServiceAddress;
        // 删除文件
        deleteFile(fileName);

        // 如果该目录不存在任何服务ip地址,将该文件夹也删除
        // ...暂时可以不删除文件夹
    }
    public void deleteFile(String fileName) {
       File file = new File(fileName);
       if (file.exists()) {
           file.delete();
           System.out.println("{" + fileName + "}删除成功...");
       }
    }
    public void deleteFolder() {
        // 可以暂时不实现
    }

    public void removeServiceAddress(String serviceName, String serviceAddress){
        ArrayList<String> temp = serviceMapList.get(serviceName);
        temp.remove(serviceAddress);
        if (temp.size() == 0) { // 如果已经没有任何服务器实现这个服务,则删除
            serviceMapList.remove(serviceName);
        } else { // 如果删除元素后还有其他的服务器,则更新服务器可用列表
            serviceMapList.put(serviceName, temp);
        }
    }

    /**
     * 注册中心进行查询操作,针对一个
     */
//    public String query(String serviceName) {
//        if (serviceMap.containsKey(serviceName)) {
//            return serviceMap.get(serviceName);
//        } else {
//            System.out.println("无法寻找到该服务:{" + serviceName + "}");
//        }
//        return null;
//    }

    /**
     * 客户端查询可用服务列表,注册中心返回列表,
     */
    public ArrayList<String> queryServiceList(String serviceName) {
        if (serviceMapList.containsKey(serviceName)) {
            return serviceMapList.get(serviceName);
        } else {
            System.out.println("无法寻找到该服务:{" + serviceName + "}");
        }
        return null;
    }
}
