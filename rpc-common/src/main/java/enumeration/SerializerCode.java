package enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化器和反序列化器编号
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    KRYO(0),
    JSON(1),
    HESSIAN(2),
    PROTOBUF(3);

    private final int code;
}
