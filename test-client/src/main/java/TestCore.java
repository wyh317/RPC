import transport.RpcClient;
import transport.RpcClientProxy;

public class TestCore {
    public static void test(RpcClient client){
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HiService hiService = proxy.getProxy(HiService.class);
        HelloObject helloObject = new HelloObject(12, "This is hello message");
        HelloObject hiObject = new HelloObject(13, "This is hi message");
        String helloRes = helloService.hello(helloObject);
        String hiRes = hiService.hi(hiObject);
        System.out.println(helloRes);
        System.out.println(hiRes);
    }
}
