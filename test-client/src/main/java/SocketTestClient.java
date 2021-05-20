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
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
