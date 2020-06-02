package cn.com.self.practice.guava;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * @author kevin
 */
@Aspect
@Component
public class RateLimitAspect {
    private final static Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);
    private RateLimiter rateLimiter = RateLimiter.create(Double.MAX_VALUE);
    /**
     * 定义切点
     * 1、通过扫包切入
     * 2、带有指定注解切入
     */
    @Pointcut("@annotation(cn.com.self.practice.guava.IRateLimit)")
    public void checkPointcut() { }

    @ResponseBody
    @Around(value = "checkPointcut()")
    public Object aroundNotice(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        //获取目标方法
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(IRateLimit.class)) {
            //获取目标方法的@IRateLimit注解
            IRateLimit rateLimit = targetMethod.getAnnotation(IRateLimit.class);
            rateLimiter.setRate(rateLimit.perSecond());
            if (!rateLimiter.tryAcquire(rateLimit.timeOut(), rateLimit.timeOutUnit())){
                logger.error("fail");
                return "fail";
            }
        }
        return pjp.proceed();
    }
}
