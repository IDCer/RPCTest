package server.impl;

import api.interfaces.RPCAnnotation;
import api.service.RemoteReckonService;

import java.io.Serializable;

@RPCAnnotation(value=RemoteReckonService.class)
public class RemoteReckonServiceImpA implements RemoteReckonService, Serializable {
    @Override
    public int sum(Integer a, Integer b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < a; i++) {
            sum += b;
        }
        return sum;
    }
}
