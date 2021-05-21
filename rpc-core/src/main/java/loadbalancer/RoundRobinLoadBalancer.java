package loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 轮询法实现负载均衡
 */
public class RoundRobinLoadBalancer implements LoadBalancer{
    /**
     * 从服务提供者列表中选出一个
     */
    private int index = 0;
    @Override
    public Instance select(List<Instance> instances) {
        if(index >= instances.size()){
            index = index % instances.size();
        }
        return instances.get(index++);
    }
}
