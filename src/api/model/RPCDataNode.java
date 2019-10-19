package api.model;

import api.interfaces.RPCSignal;

/**
 * 注册中心存储服务数据的节点
 */
public class RPCDataNode implements RPCSignal {
    // 数据类型
    private String kind;




    public RPCDataNode() {
        this.kind = "DATANODE";
    }

    @Override
    public String getKind() {
        return kind;
    }
}
