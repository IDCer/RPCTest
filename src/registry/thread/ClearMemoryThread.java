package registry.thread;

import api.interfaces.ClearMemoryConfig;
import registry.RPCRegistry;

import java.util.Map;

/**
 * 该线程用于清理失去联系的服务
 */
public class ClearMemoryThread implements Runnable {
    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for(Map.Entry<String,Long> entry: RPCRegistry.heartTable.entrySet()){
            if(currentTime-entry.getValue()> ClearMemoryConfig.timeout*1000){
                RPCRegistry.heartTable.remove(entry.getKey());
            }
        }
    }
}
