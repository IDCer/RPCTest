package api.model;

import api.interfaces.RPCSignal;
import java.io.Serializable;

public class RPCServiceQueryRequest implements RPCSignal, Serializable {
    // 请求类型
    private String kind;

    // 请求的服务名
    private String serviceName;

    public RPCServiceQueryRequest(String serviceName) {
        this.serviceName = serviceName;
        this.kind = "QUERY";
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String getKind() {
        return kind;
    }

    @Override
    public String toString() {
        return "RPCServiceQueryRequest{" +
                "kind='" + kind + '\'' +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
