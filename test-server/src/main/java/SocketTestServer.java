import serializer.HessianSerializer;
import transport.socket.server.SocketServer;

/**
 * 服务端测试（使用Java原生socket）
 */
public class SocketTestServer {
    public static void main(String[] args){
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9999);
        socketServer.setSerializer(new HessianSerializer());
        socketServer.publishService(helloService, HelloService.class);
    }
}
