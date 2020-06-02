package cn.com.self.practice.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hystrix将请求的逻辑进行封装，一种是把相关逻辑封装在独立的线程中执行实现隔离，另一种是利用信号量来控制并发数实现隔离
 * @author kevin
 */
@RestController
public class HystrixController {
    private final static Logger logger = LoggerFactory.getLogger(HystrixController.class);

    /**
     * 线程池实现
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/hystrix1",produces = "application/json;charset=UTF-8")
    @HystrixCommand(
            fallbackMethod = "error",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "10"),//10个核心线程
                    @HystrixProperty(name = "maxQueueSize", value = "100"),//最大线程数100
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "10")},//队列阈值10
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //命令执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "1000"), //若干10s一个窗口内失败1000次, 则达到触发熔断的最少请求量
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1") //断路1s后尝试执行, 默认为5s
            })
    @ResponseBody
    public String hystrix() throws InterruptedException {
        Thread.sleep(500);
        logger.info("hystrix1");
        return "hystrix1";
    }

    /**
     * 信号量实现
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/hystrix2",produces = "application/json;charset=UTF-8")
    @HystrixCommand(
            fallbackMethod = "error",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "1"),
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            })
    @ResponseBody
    public String hystrix2() throws InterruptedException {
        Thread.sleep(500);
        logger.info("hystrix2");
        return "hystrix2";
    }

    public String error() {
        logger.info("fail");
        return "fail";
    }
    /**
     * hystrix不但可以通过线程池限流，也可以通过信号量来进行限流，不过都是通过控制并发数来实现的，
     * 没有直接提供类似QPS的限流方式，不过也可以估算并相互转化。
     *
     * 一般设置标准：
     *
     * 线程池大小 = 峰值QPS * （99耗时/1s） + 预留线程数
     */
}
