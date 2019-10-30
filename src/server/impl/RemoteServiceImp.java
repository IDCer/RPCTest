package server.impl;

import api.interfaces.RPCAnnotation;
import api.service.RemoteService;

import java.io.Serializable;

/**
 * 发送信息接口的具体实现
 */

@RPCAnnotation(value=RemoteService.class)
public class RemoteServiceImp implements RemoteService, Serializable {
    /**
     * 接口的具体实现方法
     */
    @Override
    public String reverseString(String message) {
        return new StringBuilder(message).reverse().toString();
    }
}
