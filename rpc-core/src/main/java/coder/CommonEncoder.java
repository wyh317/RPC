package coder;

import entity.RpcRequest;
import enumeration.PackageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import serializer.CommonSerializer;

/**
 * 编码器类
 */
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer){
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //写入魔数
        out.writeInt(MAGIC_NUMBER);
        //写入数据包类型code
        if(msg instanceof RpcRequest)
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        else
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        //写入序列化器code
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        //写入序列化后的对象，及其字节长度
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
