package serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.RpcRequest;
import enumeration.SerializerCode;
import exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 使用JSON格式的序列化器
 */
public class JsonSerializer implements CommonSerializer{
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj){
        try {
            return objectMapper.writeValueAsBytes(obj);
        }
        catch(JsonProcessingException e){
            logger.error("序列化时有错误发生：", e);
            throw new SerializeException("序列化时有错误发生");
        }
    }
    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz){
        try{
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest){
                obj = handleRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            logger.error("反序列化时有错误产生：", e);
            throw new SerializeException("反序列化时有错误产生");
        }
    }
    @Override
    public int getCode(){
        return SerializerCode.valueOf("JSON").getCode();
    }

    //这里由于涉及使用JSON反序列化Object数组，无法保证反序列化后仍然为原实例类型，因此需要使用一个辅助函数辅助反序列化
    private Object handleRequest(Object obj) throws IOException{
        RpcRequest rpcRequest = (RpcRequest)obj;
        for(int i = 0; i < rpcRequest.getParamTypes().length; i++){
            //class代表RpcRequest请求参数中第i个参数的具体类
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())){
                //将这个参数重新序列化，并使用这个参数的具体类（而不再是Object）反序列化
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }


}
