package loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡器接口
 */
public interface LoadBalancer {
    /**
     * 从服务提供者列表中选出一个
     */
    Instance select(List<Instance> instances);
}
