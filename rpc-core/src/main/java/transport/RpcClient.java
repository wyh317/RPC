package transport;

import entity.RpcRequest;
import serializer.CommonSerializer;

/**
 * 客户端类通用接口
 */
public interface RpcClient {
    /**
     * 发送RPC调用请求
     * @param rpcRequest 请求内容
     * @return 调用结果
     */
    Object sendRequest(RpcRequest rpcRequest);

    /**
     * 设置序列化器
     * @param serializer 序列化器
     */
    void setSerializer(CommonSerializer serializer);
}
