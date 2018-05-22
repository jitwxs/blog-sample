package jit.wxs.demo.utils;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    private final static int CONNECT_TIMEOUT = 4000;// 连接超时毫秒
    private final static int SOCKET_TIMEOUT = 10000;// 传输超时毫秒
    private final static int REQUESTCONNECT_TIMEOUT = 3000;// 获取请求超时毫秒
    private final static int CONNECT_TOTAL = 200;// 最大连接数
    private final static int CONNECT_ROUTE = 20;// 每个路由基础的连接数
    private final static String ENCODE_CHARSET = "utf-8";// 响应报文解码字符集
    private final static String RESP_CONTENT = "通信失败";
    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpClient = null;

    static {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = createSSLConnSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf).register("https", sslsf).build();
        connManager = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加到200
        connManager.setMaxTotal(CONNECT_TOTAL);
        // 将每个路由基础的连接增加到20
        connManager.setDefaultMaxPerRoute(CONNECT_ROUTE);
        // 可用空闲连接过期时间,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
        connManager.setValidateAfterInactivity(30000);
        // 设置socket超时时间
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(SOCKET_TIMEOUT).build();
        connManager.setDefaultSocketConfig(socketConfig);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(REQUESTCONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return true;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (request instanceof HttpEntityEnclosingRequest) {
                    return false;
                }
                return false;
            }
        };
        httpClient = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig)
                .setRetryHandler(httpRequestRetryHandler).build();
        if (connManager != null && connManager.getTotalStats() != null) {
            logger.info("now client pool " + connManager.getTotalStats().toString());
        }
    }

    /**
     * 发送HTTP_GET请求
     *
     * @param reqURL 请求地址
     * @param param  请求参数
     * @return 远程主机响应正文
     * @see 1)该方法会自动关闭连接,释放资源
     * @see 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决
     * 定传入前是否转码
     * @see 4)该方法会自动获取到响应消息头中[Content-Type:text/html; charset=GBK]的charset值作为响应报文的
     * 解码字符集
     * @see 5)若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符
     * 集
     */
    public static String sendGetRequest(String reqURL, String param) {
        if (null != param) {
            reqURL += "?" + param;
        }
        String respContent = RESP_CONTENT; // 响应内容
        // reqURL = URLDecoder.decode(reqURL, ENCODE_CHARSET);
        HttpGet httpget = new HttpGet(reqURL);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpget, HttpClientContext.create()); // 执行GET请求
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (null != entity) {
                Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                respContent = EntityUtils.toString(entity, respCharset);
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException cte) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
        } catch (SocketTimeoutException ste) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
        } catch (ClientProtocolException cpe) {
            // 该异常通常是协议错误导致:比如构造HttpGet对象时传入协议不对(将'http'写成'htp')or响应内容不符合HTTP协议要求等
            logger.error("请求通信[" + reqURL + "]时协议异常,堆栈轨迹如下", cpe);
        } catch (ParseException pe) {
            logger.error("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下", pe);
        } catch (IOException ioe) {
            // 该异常通常是网络原因引起的,如HTTP服务器未启动等
            logger.error("请求通信[" + reqURL + "]时网络异常,堆栈轨迹如下", ioe);
        } catch (Exception e) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpget != null) {
                httpget.releaseConnection();
            }
        }
        return respContent;
    }

    public static String sendPostRequest(String reqURL, String param) {
        return sendPostRequest(reqURL, param, "");
    }

    /**
     * 发送HTTP_POST请求 type: 默认是表单请求，
     *
     * @param reqURL 请求地址
     * @param param  请求参数,若有多个参数则应拼接为param11=value11&22=value22&33=value33的形式
     * @param type   编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
     * @return 远程主机响应正文
     * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
     * @see 2)该方法会自动关闭连接,释放资源
     * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自
     * 动对其转码
     * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html; charset=GBK]的
     * charset值
     * @see 6)若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
     */
    public static String sendPostRequest(String reqURL, String param, String type) {
        String result = RESP_CONTENT;
        // 设置请求和传输超时时间
        HttpPost httpPost = new HttpPost(reqURL);
        // 这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息
        if (type != null && type.length() > 0) {
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=" + ENCODE_CHARSET);
        } else {
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + ENCODE_CHARSET);
        }
        CloseableHttpResponse response = null;
        try {
            if (param != null) {
                StringEntity entity = new StringEntity(param, ENCODE_CHARSET);
                httpPost.setEntity(entity);
            }
            logger.info("开始执行请求：" + reqURL);
            // reqURL = URLDecoder.decode(reqURL, ENCODE_CHARSET);
            response = httpClient.execute(httpPost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                result = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                logger.info("执行请求完毕：" + result);
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException cte) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
        } catch (SocketTimeoutException ste) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
        } catch (ClientProtocolException cpe) {
            logger.error("请求通信[" + reqURL + "]时协议异常,堆栈轨迹如下", cpe);
        } catch (ParseException pe) {
            logger.error("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下", pe);
        } catch (IOException ioe) {
            logger.error("请求通信[" + reqURL + "]时网络异常,堆栈轨迹如下", ioe);
        } catch (Exception e) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return result;
    }

    //SSL的socket工厂创建
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        // 创建TrustManager() 用于解决javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String authType) throws CertificateException {
                // TODO Auto-generated method stub
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String authType) throws CertificateException {
                // TODO Auto-generated method stub
            }
        };
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            sslContext.init(null, new TrustManager[]{(TrustManager) trustManager}, null);
            // 创建SSLSocketFactory , // 不校验域名 ,取代以前验证规则
            sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    public static Map<HttpRoute, PoolStats> getConnManagerStats() {
        if (connManager != null) {
            Set<HttpRoute> routeSet = connManager.getRoutes();
            if (routeSet != null && !routeSet.isEmpty()) {
                Map<HttpRoute, PoolStats> routeStatsMap = new HashMap<HttpRoute, PoolStats>();
                for (HttpRoute route : routeSet) {
                    PoolStats stats = connManager.getStats(route);
                    routeStatsMap.put(route, stats);
                }
                return routeStatsMap;
            }
        }
        return null;
    }

    public static PoolStats getConnManagerTotalStats() {
        if (connManager != null) {
            return connManager.getTotalStats();
        }
        return null;
    }

    /**
     * 关闭系统时关闭httpClient
     */
    public static void releaseHttpClient() {
        try {
            httpClient.close();
        } catch (IOException e) {
            logger.error("关闭httpClient异常" + e);
        } finally {
            if (connManager != null) {
                connManager.shutdown();
            }
        }
    }

    /**
     * url参数转Map
     * 形如：access_token=example_token&scope=&token_type=bearer
     * @author jitwxs
     * @since 2018/5/21 16:47
     */
    public static Map<String, String> params2Map(String params) {
        Map<String, String> map = new HashMap<>();

        String[] tmp = params.trim().split("&");
        for(String param : tmp) {
            String[] kv = param.split("=");
            if(kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }
}