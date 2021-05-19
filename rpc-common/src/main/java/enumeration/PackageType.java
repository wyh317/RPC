package enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议包类型（请求包或响应包）
 */
@AllArgsConstructor
@Getter
public enum PackageType {
    REQUEST_PACK(0),
    RESPONSE_PACK(1);
    private final int code;
}
