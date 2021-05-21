import loadbalancer.RandomLoadBalancer;
import serializer.KryoSerializer;
import transport.RpcClient;
import transport.RpcClientProxy;
import transport.socket.client.SocketClient;




/**
 * 客户端测试（使用Java原生Socket）
 */
public class SocketTestClient {
    public static void main(String[] args){
        RpcClient client = new SocketClient(new KryoSerializer(), new RandomLoadBalancer());
        TestCore.test(client);
    }
}
