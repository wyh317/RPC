import provider.ServiceProvider;
import provider.ServiceProviderImpl;
import serializer.ProtobufSerializer;
import transport.RpcServer;
import transport.netty.server.NettyServer;

/**
 * 服务器端测试（使用Netty）
 */
public class NettyTestServer {

    public static void main(String[] args){
        //在本地注册表上注册两个服务：helloService、hiService
        HelloService helloService = new HelloServiceImpl();
        HiService hiService = new HiServiceImpl();
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.addService(helloService);
        serviceProvider.addService(hiService);
        //创建Netty服务端
        RpcServer server = new NettyServer("127.0.0.1", 9998, serviceProvider);
        //设置服务端序列化器
        server.setSerializer(new ProtobufSerializer());
        //将本地服务注册表上的所有服务都发布到远程Nacos
        server.publishAllService();
        server.start();
    }
}
