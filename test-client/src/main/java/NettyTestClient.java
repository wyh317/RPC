import serializer.ProtobufSerializer;
import transport.RpcClient;
import transport.RpcClientProxy;
import transport.netty.client.NettyClient;

/**
 * 客户端测试（使用Netty）
 */
public class NettyTestClient {

    public static void main(String[] args){
        RpcClient client = new NettyClient();
        client.setSerializer(new ProtobufSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
