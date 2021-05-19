package util;

import entity.RPCRequest;
import entity.RPCResponse;
import enumeration.RPCError;
import enumeration.ResponseCode;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查响应和请求
 */
public class RpcMessageChecker {
    public static final String INTERFACE_NAME = "interfaceName";
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    public RpcMessageChecker(){
    }

    public static void check(RPCRequest rpcRequest, RPCResponse rpcResponse){
        //检查响应是否为空，为空则说明服务调用失败
        if(rpcResponse == null){
            logger.error("服务调用失败,serviceName:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RPCError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        //检查响应包中的RequestId和请求包中的是否相同
        if(!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())){
            throw new RpcException(RPCError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        if(rpcResponse.getStatusCode() == null || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())){
            logger.error("调用服务失败,serviceName:{},RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RPCError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
