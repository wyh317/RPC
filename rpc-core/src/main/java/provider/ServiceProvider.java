package provider;

/**
 * 本地服务注册表，保存服务端本地的服务
 */
public interface ServiceProvider {
    /**
     * 向本地服务注册表中添加一个服务
     */
    <T> void addServiceProvider(T service);

    /**
     * 通过服务接口名获得一个服务实体类
     */
    Object getServiceProvider(String serviceName);
}
