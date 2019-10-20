package server.impl;

import api.interfaces.RPCAnnotation;
import api.service.RemoteReckonService;

import java.io.Serializable;

@RPCAnnotation(value=RemoteReckonService.class)
public class RemoteReckonServiceImpB implements RemoteReckonService, Serializable {

    @Override
    public int sum(Integer a, Integer b) {
        return a * b;
    }
}
