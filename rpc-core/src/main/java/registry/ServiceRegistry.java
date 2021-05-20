package registry;

import java.net.InetSocketAddress;

/**
 * 远程注册中心（远程注册表）接口
 */
public interface ServiceRegistry {
    /**
     * 向Nacos注册服务（供服务端调用的方法）
     * @param serviceName 服务接口名
     * @param inetSocketAddress 服务提供者的地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 根据服务接口名查找服务提供者（供客户端调用的方法）
     * @param serviceName 服务接口名
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
