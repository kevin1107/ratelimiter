package cn.com.self.practice.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author kevin
 */
public class HttpClientUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 发送POST请求，参数是JSON
     */
    public static String requestPost(String url, String jsonParam) {
        String respContent = null;
        try (CloseableHttpClient client = HttpClients.createDefault()){
            logger.info("HttpTool.requestPost 开始 请求url：" + url + ", 参数：" + jsonParam);
            //创建HttpPost对象
            HttpPost httpPost = new HttpPost(url);
            //配置请求参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.DEFAULT)
                    .setExpectContinueEnabled(true)
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            httpPost.setConfig(requestConfig);
            //设置参数和请求方式
            //解决中文乱码问题
            StringEntity entity = new StringEntity(jsonParam, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            //执行请求
            HttpResponse resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseObj = resp.getEntity();
                respContent = EntityUtils.toString(responseObj, "UTF-8");
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("HttpTool.requestPost 异常 请求url：" + url + ", 参数：" + jsonParam + "，异常信息：" + e);
        }
        logger.info("HttpTool.requestPost 结束 请求url：" + url + ", 参数：" + jsonParam + "");
        //返回数据
        return respContent;
    }
}
