package transport.socket.server;

import handler.RequestHandler;
import enumeration.RpcError;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import provider.ServiceProviderImpl;
import registry.NacosServiceRegistry;
import registry.ServiceRegistry;
import serializer.CommonSerializer;
import transport.RpcServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import util.ThreadPoolFactory;
/**
 * 采用Socket方式进行响应的服务器端
 */
public class SocketServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    //服务端的线程池
    private final ExecutorService threadPool;
    //服务端的IP地址和端口号
    private final String host;
    private final int port;
    //服务端采用的序列化器
    private CommonSerializer serializer;
    private RequestHandler requestHandler = new RequestHandler();
    //远程Nacos注册表
    private final ServiceRegistry serviceRegistry;
    //本地注册表
    private final ServiceProvider serviceProvider;

    public SocketServer(String host, int port, ServiceProvider serviceProvider){
        this.port = port;
        this.host = host;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = serviceProvider;
    }


    /**
     * 启动服务端
     */
    @Override
    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器启动...");
            Socket socket;
            //接到客户端连接后,从线程池中派一个线程线程去处理
            while((socket = serverSocket.accept()) != null){
                logger.info("客户端连接：{}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生:", e);
        }
    }

    /**
     * 设置服务端的序列化器
     * @param serializer 序列化器
     */
    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 发布服务
     * @param service 某个服务实现类
     * @param serviceClass 客户端和服务端都认得的服务接口
     */
    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        //再在远程注册表Nacos上注册：告诉Nacos本服务提供者提供了什么服务（接口），地址是什么
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
    }

    /**
     * 向Nacos发布本地注册表上的所有服务
     */
    @Override
    public void publishAllService() {
        //获得本地注册表上的所有服务实体
        List<Object> serviceList = serviceProvider.getAllService();
        for(Object service : serviceList){
            //找到某一个服务实体所实现的所有接口，对每一个接口都调用一次publishService
            Class<?>[] interfaces = service.getClass().getInterfaces();
            for(Class clazz: interfaces)
                publishService(service, clazz);
        }
    }
}
