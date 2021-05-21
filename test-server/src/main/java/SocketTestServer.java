import provider.ServiceProvider;
import provider.ServiceProviderImpl;
import serializer.HessianSerializer;
import transport.socket.server.SocketServer;

/**
 * 服务端测试（使用Java原生socket）
 */
public class SocketTestServer {
    public static void main(String[] args){
        //创建本地注册表，并在上面注册两个服务：helloService、hiService
        HelloService helloService = new HelloServiceImpl();
        HiService hiService = new HiServiceImpl();
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.addService(hiService);
        serviceProvider.addService(helloService);
        //创建Socket服务端，并设置服务端序列化器
        SocketServer socketServer = new SocketServer("127.0.0.1", 9999, serviceProvider);
        socketServer.setSerializer(new HessianSerializer());
        //将本地注册表上的服务全部发布到远程注册中心Nacos
        socketServer.publishAllService();
        socketServer.start();
    }
}
