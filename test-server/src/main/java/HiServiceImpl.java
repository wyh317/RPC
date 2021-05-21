import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HiService接口的实现类
 */
public class HiServiceImpl implements HiService{

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hi(HelloObject helloObject) {
        logger.error("接收到：{}", helloObject.getMessage());
        return helloObject.getMessage() + " id=" + helloObject.getId();
    }
}
