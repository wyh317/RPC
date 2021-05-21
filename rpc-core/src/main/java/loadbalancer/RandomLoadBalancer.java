package loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * 随机法实现负载均衡
 */
public class RandomLoadBalancer implements LoadBalancer{
    /**
     * 从服务提供者列表中选出一个
     */
    @Override
    public Instance select(List<Instance> instances) {
        Random rand = new Random();
        return instances.get(rand.nextInt(instances.size()));
    }
}
