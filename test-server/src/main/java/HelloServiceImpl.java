import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HelloService接口的一个实现类
 */
public class HelloServiceImpl implements HelloService{

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object){
        logger.info("接收到：{}", object.getMessage());
        return object.getMessage() + " id=" + object.getId();
    }
}
