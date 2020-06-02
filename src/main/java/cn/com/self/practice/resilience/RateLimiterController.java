package cn.com.self.practice.resilience;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kevin
 */
@RestController
@RequestMapping("/resilience")
public class RateLimiterController extends CurrentLimiterConfig {
    private final static Logger logger = LoggerFactory.getLogger(RateLimiterController.class);
    private final RateLimiterService service = new RateLimiterService();

    @RequestMapping(method = RequestMethod.POST,value = "/rateLimiter",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String rateLimiter() {
        logger.info("enter resilience rateLimiter");
        String result = Try.ofSupplier(RateLimiter.decorateSupplier(rateLimiter, service::rateLimiterService))
                .recover(CommonException.class, "fail")
                .recover(RequestNotPermitted.class,"Request not permitted for limiter")
                .get();
        logger.info(result);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/bulkhead",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String bulkhead() {
        logger.info("enter resilience bulkhead");
        String result = Try.ofSupplier(Bulkhead.decorateSupplier(bulkhead, service::bulkheadService))
                .recover(CommonException.class, "fail")
                .recover(RequestNotPermitted.class,"Request not permitted for limiter")
                .recover(BulkheadFullException.class,"Bulkhead name is full")
                .get();
        logger.info(result);
        return result;
    }

}
