package cn.com.self.practice.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static cn.com.self.practice.util.HttpClientUtil.requestPost;

class HystrixControllerTest {
    private final static Logger logger = LoggerFactory.getLogger(HystrixControllerTest.class);
    //模拟的并发量
    private static final int CONCURRENT_NUM = 42;
//    private static String url = "http://localhost:8080/hystrix1";
    private static String url = "http://localhost:8080/hystrix2";
    private static CountDownLatch cdl = new CountDownLatch(CONCURRENT_NUM);
    public static void main(String[] args) {
        for (int i = 0; i < CONCURRENT_NUM; i++) {
            new Thread(new Demo()).start();
            cdl.countDown();
        }
    }
    public static class Demo implements Runnable{
        @Override
        public void run() {
            try {
                cdl.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //使用工具类发送http请求

            String json2 = requestPost(url, "");
            logger.info(new Date().getTime()+"::"+json2);
        }

    }

}