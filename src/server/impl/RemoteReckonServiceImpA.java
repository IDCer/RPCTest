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
        int one = 1;
        if (a * b < 0) {
            one = -1;
        }
        int a1 = Math.abs(a);
        int b1 = Math.abs(b);
        int sum = 0;
        for (int i = 0; i < a1; i++) {
            sum += b1;
        }
        return sum * one;
    }
}
