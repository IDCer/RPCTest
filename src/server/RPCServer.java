package server;

import api.interfaces.HeartBeatConfig;
import api.interfaces.RPCAnnotation;
import api.interfaces.RPCRegistryCenter;
import api.interfaces.RPCRegistryCenterConfig;
import server.thread.HeartBeatThread;
import server.thread.ServerThread;
import server.thread.ServerThreadNew;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RPCServer {
    /**
     * a Map struct, use to store the node's info
     */
    private static final HashMap<String, Object> serviceMap = new HashMap<String, Object>();

    // registry center
    private RPCRegistryCenter registryCenter;

    // service address
    private String addressService;

    // construct function
    public RPCServer(RPCRegistryCenter rpcRegistryCenter, String addressService) {
        this.registryCenter = rpcRegistryCenter;
        this.addressService = addressService;
    }

    public RPCServer(String addressService) {
        this.addressService = addressService;
    }

    // 开启服务器
    public void start() {
        // get the port
        int port = Integer.parseInt(addressService.split(":")[1]);

        System.out.println("register center to make the service persistent...");

        // register service
        try (ServerSocket serverSocket = new ServerSocket(port)){
//            for(String interfaceName : serviceMap.keySet()) {
//                registryCenter.register(interfaceName, addressService);
//                System.out.println("register successful!service name:{" + interfaceName + "},service address:{" + addressService + "}");
//            }
            //定时发送心跳包
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(new HeartBeatThread(this),0, HeartBeatConfig.sendIntervalTime, TimeUnit.SECONDS);

            while (true) {
                // stop to listen the client thread
                System.out.println("wait for a client...");
                Socket socket = serverSocket.accept();
                System.out.println("receive a client request -> " + socket.getLocalAddress() + ":" + socket.getLocalPort());
                // start a new thread to handle
                new Thread(new ServerThread(socket, serviceMap)).start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 向注册中心注册服务
//    public void bind(Object... services) {
//        System.out.println("begin register the service...");
//        for (Object service : services) {
//            // get the export interface
//            System.out.println("service is:" + service);
//            RPCAnnotation rpcAnnotation = service.getClass().getAnnotation(RPCAnnotation.class);
//
//            if (rpcAnnotation == null) {
//                System.out.println("rpcAnnotation:" + rpcAnnotation);
//                continue;
//            }
//            // the service name
//            String serviceName = rpcAnnotation.value().getName();
//
//            // 通过socket传输数据到注册中心注册服务
//            String [] ipAndPort = RPCRegistryCenterConfig.registryAddress.split(":");
//            try {
//                // 构建与注册中心的socket连接
//                Socket socket = new Socket(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
//
////                // 获取输出流
////                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
////                // 将对象传输给注册中心
////                objectOutputStream.writeObject(obj);
////                objectOutputStream.flush();
////                socket.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void bind(Object... services) {
        System.out.println("begin register the service...");
        for (Object service : services) {
            // get the export interface
            System.out.println("service is:" + service);
            RPCAnnotation rpcAnnotation = service.getClass().getAnnotation(RPCAnnotation.class);

            if (rpcAnnotation == null) {
                System.out.println("rpcAnnotation:" + rpcAnnotation);
                continue;
            }

            // the service name
            String serviceName = rpcAnnotation.value().getName();

            // add to the service map
            serviceMap.put(serviceName, service);
            System.out.println("service bind successful! => {" + serviceName + " : " + service + "}");
            System.out.println("服务列表为:" + serviceMap);
        }
    }

    public static Map<String, Object> getServiceMap() {
        return serviceMap;
    }

    public String getAddressService() {
        return addressService;
    }
}
