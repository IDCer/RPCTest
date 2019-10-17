package api.interfaces;

/**
 * 注册中心抽象接口
 */
public interface RPCRegistryCenter {
    public void register(String serviceName, String serviceAddress);
}
