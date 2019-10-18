package api.model;

import api.interfaces.RPCSignal;
import java.io.Serializable;

public class RPCRegisterRequest implements RPCSignal, Serializable {
    // 请求类型
    private String kind;

    // 注册请求来源,服务提供来源
    private String serviceAddress;

    // 传给注册中心的服务请求
    private String serviceName;

    // 构造函数
    public RPCRegisterRequest(String serviceName, String serviceAddress) {
        this.serviceName = serviceName;
        this.serviceAddress = serviceAddress;
        // 标注类型
        this.kind = "REGISTER";
    }

    @Override
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "RPCRegisterRequest{" +
                "kind='" + kind + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
