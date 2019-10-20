package api.service;

import api.interfaces.RPCAnnotation;

/**
 * 发送信息服务接口,接口的函数说明,如参数和返回值都需要进行规范
 */
//@RPCAnnotation(value=RemoteService.class)
public interface RemoteService {
    public String sendMessage(String message);
}
