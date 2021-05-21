package registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import enumeration.RpcError;
import exception.RpcException;
import loadbalancer.LoadBalancer;
import loadbalancer.RandomLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Nacos服务注册中心
 */
public class NacosServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);
    //Nacos地址
    private static final String SERVER_ADDR = "127.0.0.1:8848";
    private static final NamingService namingService;
    //负载均衡器
    private final LoadBalancer loadBalancer;
    static {
        //通过 NamingFactory 创建 NamingService 连接 Nacos
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            logger.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }
    public NacosServiceRegistry(){
        this.loadBalancer = new RandomLoadBalancer();
    }
    public NacosServiceRegistry(LoadBalancer loadBalancer){
         this.loadBalancer = loadBalancer;
    }

    /**
     * （服务提供者）向Nacos注册服务
     * @param serviceName 服务接口名
     * @param inetSocketAddress 服务提供者的地址
     */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try{
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    /**
     * 根据服务接口名查找服务提供者
     * @param serviceName 服务接口名
     * @return 服务提供者地址
     */
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try{
            List<Instance> instacnes = namingService.getAllInstances(serviceName);
            //根据客户端提供的负载均衡器从服务提供者列表中选择出一个
            Instance instance = loadBalancer.select(instacnes);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
