package cn.com.self.practice.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * RateLimiter使用的是一种叫令牌桶的流控算法，
 * RateLimiter会按照一定的频率往桶里扔令牌，线程拿到令牌才能执行，
 * 比如你希望自己的应用程序QPS不要超过1000，那么RateLimiter设置1000的速率后，就会每秒往桶里扔1000个令牌。
 * @author kevin
 */
@RestController
@RequestMapping("/guava")
public class GuavaController {
    private final static Logger logger = LoggerFactory.getLogger(GuavaController.class);

    @RequestMapping(method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @IRateLimit(perSecond = 10)
    @ResponseBody
    public String guava() throws InterruptedException {
        logger.info("guava");
        Thread.sleep(20);
        return "guava";
    }

}
