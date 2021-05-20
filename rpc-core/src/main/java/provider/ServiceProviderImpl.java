package provider;

import enumeration.RpcError;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProviderImpl implements ServiceProvider{
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);
    //key为服务接口，value为一个实现它的服务实体对象
    //例如一个服务对象A实现了X和Y两个接口，那么注册完A后，map中会有两条映射
    //X -> A
    //Y -> A
    //一个服务对象可以实现多个接口，但一个接口只能由一个服务实体对象提供服务
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    //registeredService作为一个set，保存已经注册好的服务名
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();


    @Override
    public <T> void addServiceProvider(T service) {
        String serviceName = service.getClass().getCanonicalName();
        //如果service已经注册过了，直接返回，否则将这个服务类名加入到set中
        if(registeredService.contains(serviceName))
            return;
        registeredService.add(serviceName);
        //获得这个服务实体类service所实现的接口
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        //将每一个接口名和服务实体的映射写入到map中
        for(Class<?> i : interfaces){
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("向接口: {} 注册服务: {}", interfaces, serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
