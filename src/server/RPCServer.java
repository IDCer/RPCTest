package server;

import api.interfaces.RPCAnnotation;
import api.interfaces.RPCRegistryCenter;
import server.thread.ServerThread;

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

    // export service
    public void export() {
        // get the port
        int port = Integer.parseInt(addressService.split(":")[1]);

        System.out.println("register center to make the service persistent...");

        // register service
        try (ServerSocket serverSocket = new ServerSocket(port)){
            for(String interfaceName : serviceMap.keySet()) {
                registryCenter.register(interfaceName, addressService);
                System.out.println("register successful!service name:{" + interfaceName + "},service address:{" + addressService + "}");
            }
            //定时发送心跳包
//            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//            service.scheduleAtFixedRate(new HeartbeatClient(this),0,1, TimeUnit.MINUTES);

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

    // bind service name and service object
    public void bind(Object... services) {
        System.out.println("begin bind the service...");
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
            System.out.println("service bind successful! => {" + serviceName + ":" + service + "}");
        }
    }

    public static Map<String, Object> getHandlerMapping() {
        return serviceMap;
    }

    public String getAddressService() {
        return addressService;
    }
}
