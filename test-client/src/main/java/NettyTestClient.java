import serializer.ProtobufSerializer;
import transport.RpcClient;
import transport.RpcClientProxy;
import transport.netty.client.NettyClient;

/**
 * 客户端测试（使用Netty）
 */
public class NettyTestClient {

    public static void main(String[] args){
        //创建Netty客户端，并设置客户端序列化器
        RpcClient client = new NettyClient();
        client.setSerializer(new ProtobufSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HiService hiService = proxy.getProxy(HiService.class);
        HelloObject helloObject = new HelloObject(12, "This is hello message");
        HelloObject hiObject = new HelloObject(13, "This is hi message");
        String helloRes = helloService.hello(helloObject);
        String hiRes = hiService.hi(hiObject);
        System.out.println(helloRes);
        System.out.println(hiRes);
    }
}
