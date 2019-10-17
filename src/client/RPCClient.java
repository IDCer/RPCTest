package client;

import api.interfaces.RPCServiceDiscovery;
import client.proxy.RPCClientProxy;

import java.lang.reflect.Proxy;

/**
 * 客户端返回服务的代理实现对象
 */
public class RPCClient {

    // 服务发现中心
    private RPCServiceDiscovery rpcServiceDiscovery;
    public RPCClient(){}
    // 构造函数,传入服务发现中心
    public RPCClient(RPCServiceDiscovery rpcServiceDiscovery) {
        this.rpcServiceDiscovery = rpcServiceDiscovery;
    }

    // 返回一个服务的代理对象,这里只是一个接口,具体实现将放在服务端
    public <T> T getProxy(final Class<T> interfaceClass, final String host, final int port)  {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new RPCClientProxy(host, port));
    }

    public <T> T getProxy(final Class<T> interfaceClass)  {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new RPCClientProxy(rpcServiceDiscovery));
    }
}
