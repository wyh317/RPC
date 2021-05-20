package transport.socket.util;

import entity.RpcRequest;
import enumeration.PackageType;
import serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

public class ObjectWriter {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    /**
     * 将一个RPC对象包装成协议包并序列化后写入输出流中
     * @param outputStream 输出流
     * @param object RPC对象（RPCRequest或RPCResponse）
     * @param serializer 序列化器
     */
    public static void writeObject(OutputStream outputStream, Object object, CommonSerializer serializer) throws IOException{
        //写入魔数
        outputStream.write(intToBytes(MAGIC_NUMBER));
        //写入RPC对象的类型code（响应包或请求包）
        if(object instanceof RpcRequest){
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        }
        else{
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
        //写入序列化器的code
        outputStream.write(intToBytes(serializer.getCode()));
        byte[] bytes = serializer.serialize(object);
        //写入data域的大小（对于防止粘包，这是有必要的）
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }

    private static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
}
