package api.service;

import api.interfaces.RPCAnnotation;

/**
 * 发送信息服务接口
 */
@RPCAnnotation(value=MessageService.class)
public interface MessageService {
    public String sendMessage(String message);
}
