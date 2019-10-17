package api.interfaces;

/**
 * 将根据接口名称发现服务调用地址
 */

public interface RPCServiceDiscovery {
    public String discover(String serviceName);
}
