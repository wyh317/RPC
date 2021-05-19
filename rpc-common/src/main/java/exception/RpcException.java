package exception;
import enumeration.RPCError;

/**
 * RPC调用异常
 */
public class RpcException extends RuntimeException{
    public RpcException(RPCError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RPCError error) {
        super(error.getMessage());
    }
}
