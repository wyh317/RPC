package entity;

import enumeration.ResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务端返回给客户端的结果对象
 */
@Data
@NoArgsConstructor
public class RPCResponse<T> implements Serializable {
    /**
     * 响应对应的请求Id
     */
    private String requestId;
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态补充信息（失败时才有）
     */
    private String message;
    /**
     * 响应数据（成功时才有）
     */
    private T data;

    public static <T> RPCResponse<T> success(T data, String requestId){
        RPCResponse<T> response = new RPCResponse<T>();
        response.setRequestId(requestId);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RPCResponse<T> fail(ResponseCode code, String requestId){
        RPCResponse<T> response = new RPCResponse<T>();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
