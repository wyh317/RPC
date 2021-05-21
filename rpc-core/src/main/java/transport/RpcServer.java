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
     * 发布某项服务
     */
    <T> void publishService(Object service, Class<T> serviceClass);

    /**
     * 向Nacos发布本地注册表上的所有服务
     */
    void publishAllService();
}
