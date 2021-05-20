import serializer.ProtobufSerializer;
import transport.RpcServer;
import transport.netty.server.NettyServer;

/**
 * 服务器端测试（使用Netty）
 */
public class NettyTestServer {

    public static void main(String[] args){
        HelloService helloService = new HelloServiceImpl();
        RpcServer server = new NettyServer("127.0.0.1", 9999);
        server.setSerializer(new ProtobufSerializer());
        server.publishService(helloService, HelloService.class);
    }
}
