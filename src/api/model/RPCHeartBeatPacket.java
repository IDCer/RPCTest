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
    private String serviceAddress;

    /**
     * 该服务器下说提供的服务列表
     */
    private ArrayList<String> serviceList;

    public RPCHeartBeatPacket(long time, String serviceAddress, ArrayList<String> serviceList) {
        this.time = time;
        this.serviceAddress = serviceAddress;
        this.serviceList = serviceList;
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

    public ArrayList<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(ArrayList<String> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public String toString() {
        return "RPCHeartBeatPacket{" +
                "kind='" + kind + '\'' +
                ", time=" + time +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", serviceList=" + serviceList +
                '}';
    }
}
