package api.service;

import api.interfaces.RPCAnnotation;

/**
 * 发送信息服务接口
 */
@RPCAnnotation(value=RemoteService.class)
public interface RemoteService {
    public String sendMessage(String message);
}
