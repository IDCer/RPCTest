package api.interfaces;

import java.util.ArrayList;

/**
 * 负载均衡接口
 */
public interface RPCLoadBalance {
    public String selectHost(ArrayList<String> repos);
}
