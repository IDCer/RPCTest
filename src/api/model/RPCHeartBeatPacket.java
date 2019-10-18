package api.model;

import api.interfaces.RPCSignal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RPCHeartBeatPacket implements RPCSignal, Serializable{
    /**
     * 信号类型
     */
    private String kind;

    /**
     * 发送心跳包的时间
     */
    private long time;

    /**
     * 服务提供的地址
     */
    private String serviceAddress; // ip + port

    public RPCHeartBeatPacket(long time, String serviceAddress) {
        this.time = time;
        this.serviceAddress = serviceAddress;
        this.kind = "HEART";
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
                ", serviceAddress='" + serviceAddress + '\'' +
                '}';
    }
}
