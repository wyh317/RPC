package transport.socket.client;

import entity.RpcRequest;
import entity.RpcResponse;
import enumeration.ResponseCode;
import enumeration.RpcError;
import exception.RpcException;
import loadbalancer.LoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.NacosServiceRegistry;
import registry.ServiceRegistry;
import serializer.CommonSerializer;
import transport.RpcClient;
import transport.socket.util.ObjectReader;
import transport.socket.util.ObjectWriter;
import util.RpcMessageChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 采用Socket方式进行调用的客户端
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private CommonSerializer serializer;
    private final ServiceRegistry serviceRegistry;

    //在构造客户端的时候，就要设置好序列化器和负载均衡器
    public SocketClient(CommonSerializer serializer, LoadBalancer loadBalancer){
        this.serializer = serializer;
        this.serviceRegistry = new NacosServiceRegistry(loadBalancer);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        //先处理corner case
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        //通过远程注册表Nacos找到能提供服务的服务提供者，并返回它的地址（IP+端口号）
        InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
        try(Socket socket = new Socket()){
            //连接到服务端
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            //调用ObjectWriter的writeObject向输出流中写入经过序列化后的rpcRequest
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            //从输入流中读入RpcResponse对象
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            //处理返回rpcResponse为空，或者状态码非200（SUCCESS）的情况
            if (rpcResponse == null) {
                logger.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            //检查响应包和请求包是否对应
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            //如果均无上述问题，则返回响应包中的data字段
            return rpcResponse.getData();
        } catch(IOException e){
            logger.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }

}
