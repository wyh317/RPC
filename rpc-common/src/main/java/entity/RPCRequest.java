package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户端向服务端发送的请求对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RPCRequest implements Serializable {
    /**
     * 请求Id
     */

    private String requestId;
    /**
     * 要调用的接口名称
     */
    private String interfaceName;
    /**
     * 要调用的方法名称
     */
    private String methodName;
    /**
     * 要调用方法的具体参数
     */
    private Object[] parameters;
    /**
     * 要调用方法的参数类型
     */
    private Class<?>[] paramTypes;

    public String getName() {
        return requestId;
    }

}
