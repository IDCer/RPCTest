package registry.thread;

import api.config.RPCConfig;
import registry.RPCRegistry;
import java.util.Map;

/**
 * 该线程用于清理失去联系的服务
 */
public class ClearMemoryThread implements Runnable {

    // 持有一个注册中心对象
    private RPCRegistry rpcRegistry;

    public ClearMemoryThread() {}

    public ClearMemoryThread(RPCRegistry rpcRegistry) {
        this.rpcRegistry = rpcRegistry;
    }

    @Override
    public void run() {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        // 遍历心跳表中所有的服务器地址,如果出现超时,则除去
        for(Map.Entry<String, Long> entry : RPCRegistry.heartTable.entrySet()){
            // 如果超过时间检测不到心跳,则从心跳表中移除这个实体,并且实际节点也需要删除
            if(currentTime - entry.getValue() > RPCConfig.timeout * 1000){
                // 心跳表移除
                System.out.println("服务器{" + entry.getKey() + "}无响应,已经将其从注册中心上移除...");
                RPCRegistry.heartTable.remove(entry.getKey());

                // 结点删除
                rpcRegistry.clear(entry.getKey());
            }
        }
    }
}
