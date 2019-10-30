package api.config;

/**
 * 用来记录一些配置文件的参数
 */
public interface RPCConfig {
    /**
     * 服务器心跳包发送间隔时间,单位为秒
     */
    public long sendIntervalTime = 3;           // 10

    /**
     * 注册中心认为心跳包失效的间隔时间,三倍于心跳发送的间隔时间,单位秒
     */
    public final long timeout = 2 * sendIntervalTime; // 3

    /**
     * 注册中心清理失效节点的间隔时间,单位秒
     */
    public final long clearIntervalTime = 5;    // 60

    /**
     * 注册中心ip地址
     */
//    public String registryAddress = "222.20.79.210:2181";
    public String registryAddress = "127.0.0.1:2181";

    /**
     * 服务器ip地址
     */
//    public String serverAddress1 = "222.20.79.210:11225";
    public String serverAddress1 = "127.0.0.1:11225";
//    public String serverAddress2 = "222.20.79.210:11234";
    public String serverAddress2 = "127.0.0.1:11234";

    /**
     * 注册中心服务节点存储地址
     */
    public String nodeNameSpace = "src/registry/namespace/";

    /**
     * 注册中心输出头
     */
    public String registryHead = "[Registry] ";

    /**
     * 服务器输出头
     */
    public String serverHead = "[Server] ";

    /**
     * 客户端输出头
     */
    public String clientHead = "[Client] ";


}
