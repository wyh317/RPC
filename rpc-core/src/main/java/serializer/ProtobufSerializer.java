package serializer;

import enumeration.SerializerCode;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用ProtoBuf的序列化器
 */
public class ProtobufSerializer implements CommonSerializer{
    //申请一个内存空间用户缓存,避免每次序列化都重新申请Buffer空间
    private LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    //缓存Schema,Schema可以理解为一个组织结构，就好比是数据库中的表、视图等等这样的组织机构，在这里表示的就是序列化对象的结构
    private Map<Class<?>, Schema<?>> schemaMap = new ConcurrentHashMap<>();


    @Override
    public byte[] serialize(Object obj) {
        //首先获得要序列化对象的类，然后为其分配一个缓存空间，其次获得这个类的Schema。ProtostuffIOUtil.toByteArray进行序列化。
        Class clazz = obj.getClass();
        Schema schema = getSchema(clazz);
        byte[] data;
        try{
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        }
        finally{
            buffer.clear();
        }
        return data;
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        //根据序列化对象的class获取其组织结构Schema。然后根据byte直接mergeFrom成一个对象。
        Schema schema = getSchema(clazz);
        Object obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    //根据一个class对象获取它的组织结构Schema
    private Schema getSchema(Class<?> clazz) {
        Schema schema = schemaMap.get(clazz);
        if(Objects.isNull(schema)){
            // 这个schema通过RuntimeSchema进行懒创建并缓存
            // 所以可以一直调用RuntimeSchema.getSchema(),这个方法是线程安全的
            schema = RuntimeSchema.getSchema(clazz);
            if(Objects.nonNull(schema))
                schemaMap.put(clazz, schema);
        }
        return schema;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("PROTOBUF").getCode();
    }
}
