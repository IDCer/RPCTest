package api.model;


import api.interfaces.RPCSignal;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 客户端和服务端传输简讯的介质
 */

public class RPCRequest implements RPCSignal, Serializable {
    // 请求类名
    private String kind;

    // 类名
    private String className;

    // 方法名
    private String methodName;

    // 参数列表
    private Object [] parameters;

    public RPCRequest() {
        this.kind = "REQUEST";
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "RPCRequest{" +
                "kind='" + kind + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
