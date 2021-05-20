package transport;

import serializer.CommonSerializer;

/**
 * 服务端类通用接口
 */
public interface RpcServer {
    /**
     * 服务端启动
     */
    void start();

    /**
     * 设置序列化器
     * @param serializer 序列化器
     */
    void setSerializer(CommonSerializer serializer);

    /**
     * 发布服务
     */
    <T> void publishService(Object service, Class<T> serviceClass);
}
