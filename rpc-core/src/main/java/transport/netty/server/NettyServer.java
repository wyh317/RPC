package transport.netty.server;

import coder.CommonDecoder;
import coder.CommonEncoder;
import enumeration.RpcError;
import exception.RpcException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import provider.ServiceProviderImpl;
import registry.NacosServiceRegistry;
import registry.ServiceRegistry;
import serializer.CommonSerializer;
import transport.RpcServer;

import java.net.InetSocketAddress;

/**
 * 采用Netty方式实现的服务器端
 */
public class NettyServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    //服务端的地址和端口号
    private final String host;
    private final int port;
    //Nacos上的远程注册表
    private final ServiceRegistry serviceRegistry;
    //本地注册表
    private final ServiceProvider serviceProvider;
    //服务器端采用的序列化器
    private CommonSerializer serializer;

    public NettyServer(String host, int port){
        this.host = host;
        this.port = port;
        serviceRegistry  = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl() ;
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
        //先在本地注册表上注册服务（具体实现）
        serviceProvider.addServiceProvider(service);
        //再在远程注册表Nacos上注册：告诉Nacos本服务提供者提供了什么服务（接口），地址是什么
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    /**
     * 启动服务端
     */
    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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

}
