package api.interfaces;

public interface ClearMemoryConfig {
    public final long timeout=3*HeartBeatConfig.sendIntervalTime;//注册中心认为心跳包失效的间隔时间，三倍于心跳发送的间隔时间，单位秒
    public final long clearIntervalTime=60;//注册中心清理失效节点的间隔时间，单位秒
}
