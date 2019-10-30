package api.service;

import api.interfaces.RPCAnnotation;

/**
 * 远程计算服务
 */
@RPCAnnotation(value=RemoteReckonService.class)
public interface RemoteReckonService {
    /**
     * 输入两个整数,返回这两个整数的和
     */
    public int sum(Integer a, Integer b);

    public String sum = "该函数输入为两个整数,输出为两个整数之和.";
}
