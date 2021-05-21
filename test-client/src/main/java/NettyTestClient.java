import loadbalancer.RandomLoadBalancer;
import loadbalancer.RoundRobinLoadBalancer;
import serializer.CommonSerializer;
import serializer.ProtobufSerializer;
import transport.RpcClient;
import transport.RpcClientProxy;
import transport.netty.client.NettyClient;

/**
 * 客户端测试（使用Netty）
 */
public class NettyTestClient {

    public static void main(String[] args){
        //创建Netty客户端，并设置负载均衡器以及客户端处的序列化器
        RpcClient client = new NettyClient(new ProtobufSerializer(), new RandomLoadBalancer());
        TestCore.test(client);
    }

}
