package cn.com.self.practice.resilience;

import org.springframework.stereotype.Service;

/**
 * @author kevin
 */
@Service
public class RateLimiterService {
    public String rateLimiterService() {
        return "resilience rateLimiter service success.";
    }

    public String bulkheadService() {
        return "resilience bulkhead service success.";
    }
}
