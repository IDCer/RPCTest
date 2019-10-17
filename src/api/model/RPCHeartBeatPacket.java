package api.model;

import api.interfaces.RPCSignal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RPCHeartBeatPacket implements RPCSignal, Serializable{
    // 信号类型
    private String kind = "HEART";
    // 发送心跳包的时间
    private long time;
    private Map<String, Object> info = new HashMap<String, Object>();
    private String serviceAddress; // ip + port

    public RPCHeartBeatPacket(long time, Map<String, Object> info, String serviceAddress) {
        this.time = time;
        this.info = info;
        this.serviceAddress = serviceAddress;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String srcAdress) {
        this.serviceAddress = srcAdress;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "RPCHeartBeatPacket{" +
                "kind='" + kind + '\'' +
                ", time=" + time +
                ", info=" + info +
                ", serviceAddress='" + serviceAddress + '\'' +
                '}';
    }
}
