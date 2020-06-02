package cn.com.self.practice.resilience;

/**
 * @author kevin
 */
public class CommonException extends RuntimeException {
    public CommonException(String errorMsg){
        super(errorMsg);
    }
}
