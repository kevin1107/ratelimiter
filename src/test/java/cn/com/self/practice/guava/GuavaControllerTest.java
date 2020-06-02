package cn.com.self.practice.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static cn.com.self.practice.util.HttpClientUtil.requestPost;

class GuavaControllerTest {

    private final static Logger logger = LoggerFactory.getLogger(GuavaControllerTest.class);
    //模拟的并发量
    private static final int CONCURRENT_NUM = 25;
    private static String url = "http://localhost:8080/guava";
    private static CountDownLatch cdl = new CountDownLatch(CONCURRENT_NUM);

    public static void main(String[] args) {
        for (int i = 0; i < CONCURRENT_NUM; i++) {
            new Thread(new Task()).start();
            cdl.countDown();
        }
    }

    public static class Task implements Runnable {
        @Override
        public void run() {
            try {
                cdl.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //使用工具类发送http请求
            String json2 = requestPost(url, "");
            logger.info(new Date().getTime() + "::" + json2);
        }
    }
}