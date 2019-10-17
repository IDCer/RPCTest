package api.interfaces;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface RPCLoadBalance {
    public String selectHost(List<String> repos);
}
