package api.model;

import api.interfaces.RPCSignal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RPCHeartBeatPacket implements RPCSignal, Serializable{
    // 信号类型
    private String kind = "HEART";
    // 发送心跳包的时间
    private long time;
    private List<String> info=new ArrayList<String>();
    private String serviceAddress; // ip + port

    public RPCHeartBeatPacket(long time, List<String> info, String serviceAddress) {
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

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
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
