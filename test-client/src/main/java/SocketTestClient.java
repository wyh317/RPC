import serializer.KryoSerializer;
import transport.RpcClientProxy;
import transport.socket.client.SocketClient;

/**
 * 客户端测试（使用Java原生Socket）
 */
public class SocketTestClient {
    public static void main(String[] args){
        SocketClient client = new SocketClient();
        client.setSerializer(new KryoSerializer());
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
