package util.webSave;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 2016/4/28.
 */
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final Header[] defaultHeaders;
    public static final Pattern ipStringPattern = Pattern.compile("([\\d]{1,3})\\.([\\d]{1,3})\\.([\\d]{1,3})\\.([\\d]{1,3})");

    private final int maxTotalThread = 1024 * 2;
    private final long connTimeToLive = 2 * 3600l;
    private final int bufferSize = 64 * 1024;

    private final CloseableHttpClient httpClient;
    private final PoolingHttpClientConnectionManager connManager;

    //    static {
//        defaultHeaders = new Header[6];
//        defaultHeaders[0] = new BasicHeader(
//                "User-Agent",
//                "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 ( .NET CLR 3.5.30729; .NET4.0C)");
//        defaultHeaders[1] = new BasicHeader("Accept",
//                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        defaultHeaders[2] = new BasicHeader("Accept-Charset", "utf-8;q=0.7,*;q=0.7");
//        defaultHeaders[3] = new BasicHeader("Content", "text/html,charset=GBK");
//        defaultHeaders[4] = new BasicHeader("Connection", "keep-alive");
//        defaultHeaders[5] = new BasicHeader("Keep-Alive", "115");
//    }
    static {
        defaultHeaders = new Header[6];
        defaultHeaders[0] = new BasicHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
        defaultHeaders[1] = new BasicHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        defaultHeaders[2] = new BasicHeader("Accept-Charset", "utf-8;q=0.7,*;q=0.7");
        defaultHeaders[3] = new BasicHeader("Content", "text/html,charset=GBK");
        defaultHeaders[4] = new BasicHeader("Connection", "keep-alive");
        defaultHeaders[5] = new BasicHeader("Keep-Alive", "115");
    }


    public HttpClientUtil() {
        connManager = getConnectManager(bufferSize, maxTotalThread, connTimeToLive);
        httpClient = createHttpClient(connManager);
    }

    public static CloseableHttpClient createHttpClient(PoolingHttpClientConnectionManager manager) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60000)
                .setSocketTimeout(0)
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .build();

        return HttpClients.custom()
                .setConnectionManager(manager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }


    public CloseableHttpResponse getResponse(HttpUriRequest requestBase, HttpContext context) throws IOException {
        return httpClient.execute(requestBase, context);
    }

    public static void setHeaders(HttpUriRequest requestBase, Header[] headers) {
        for (Header header : headers) {
            requestBase.addHeader(header.getName(), header.getValue());
        }
    }

    public static void setHeaders(HttpUriRequest requestBase) {
        setHeaders(requestBase, defaultHeaders);
    }

    public String getResponseContent(String url, String entityCharset) throws IOException {
        HttpContext context = HttpClientContext.create();
        return getResponseContent(url, context, entityCharset);
    }

    public String getResponseContent(String url, HttpContext context, String entityCharset) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet);
        return executeRequestAndGetResponseContent(httpGet, context, entityCharset);
    }

    public String getResponseContentByPost(String url, HttpContext context, HttpEntity entity, String entityCharset) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        setHeaders(httpPost);
        return executeRequestAndGetResponseContent(httpPost, context, entityCharset);
    }

    public String executeRequestAndGetResponseContent(HttpUriRequest requestBase,
                                                      HttpContext context, String entityCharset) throws IOException {
        String result;
        try (CloseableHttpResponse response = httpClient.execute(requestBase, context)) {
            //System.out.println("!!!get executed");
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("Status code for "
                        + requestBase.getURI()
                        + " is not 200 but "
                        + response.getStatusLine().getStatusCode());
                return "Error";
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                if (entityCharset == null) {
                    result = EntityUtils.toString(entity);
                } else {
                    result = EntityUtils.toString(entity, entityCharset);
                }
                return result;
            } else {
                return "";
            }
        }
    }


    public long getResponseLength(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet);
        HttpContext context = HttpClientContext.create();
        return executeRequestAndGetResponseLength(httpGet, context);
    }

    public long executeRequestAndGetResponseLength(HttpUriRequest requestBase,
                                                   HttpContext context) throws IOException {

        CloseableHttpResponse response = httpClient.execute(requestBase, context);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new IOException("statusCode=" + statusCode);
        }
        try {
            //System.out.println("get executed");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //return entity.getContentLength();
                return EntityUtils.toByteArray(entity).length;
            } else {
                return 0l;
            }
        } finally {
            response.close();
        }
    }


    public int getStatusCode(String url, HttpContext context, String entityCharset) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet);
        return executeRequestAndGetStatusCode(httpGet, context);
    }


    public int getStatusCode(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet);
        HttpContext context = HttpClientContext.create();
        return executeRequestAndGetStatusCode(httpGet, context);
    }

    public int executeRequestAndGetStatusCode(HttpRequestBase requestBase,
                                              HttpContext context) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(requestBase, context)) {
            //System.out.println("get executed");
            return response.getStatusLine().getStatusCode();
        }
    }


    private static void test1() {
        HttpHost target = new HttpHost("www.verisign.com", 443, "https");
        HttpHost proxy = new HttpHost("localhost", 8080);

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        HttpGet httpget0 = new HttpGet("/");
        httpget0.setConfig(config);

    }


    public static void main(String[] args) throws IOException {


        CloseableHttpClient client = createHttpClient(getConnectManager(1024, 12, 10000));
        HttpGet httpget = new HttpGet("http://www.apache.org/");
        HttpContext context = HttpClientContext.create();
        try (CloseableHttpResponse response = client.execute(httpget, context)) {
            //System.out.println("get executed");
            // get the response body as an array of bytes
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                byte[] bytes = EntityUtils.toByteArray(entity);
                //System.out.println("Result:" + result);
            }
        }
    }


    public void closeExpiredConnections() {
        connManager.closeExpiredConnections();
        connManager.getTotalStats();
    }

    public PoolStats getTotalStats() {
        return connManager.getTotalStats();
    }


    /**
     * Performs general setup.
     * This should be called only once.
     */
    public static PoolingHttpClientConnectionManager getConnectManager(int bufferSize, int maxTotalThread, long connTimeToLive) {


        // Client HTTP connection objects when fully initialized can be bound to
        // an arbitrary network socket. The process of network socket initialization,
        // its connection to a remote address and binding to a local one is controlled
        // by a connection socket factory.

        logger.info("bufferSize      :" + bufferSize);
        logger.info("maxTotalThread  :" + maxTotalThread);
        logger.info("connTimeToLive  :" + connTimeToLive);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        // Use custom DNS resolver to override the system DNS resolution.
/*    DnsResolver dnsResolver = new SystemDefaultDnsResolver() {


      InetAddress wwwminiapcnIp = null;
      InetAddress dnsminiapcnIp = null;

      {
        String wwwminiapcnIpString = JBoxConf.get(Constants.DNS_HOST_www_miniap_cn_KEY);
        String dnsminiapcnIpString = JBoxConf.get(Constants.DNS_HOST_dns_miniap_cn_KEY);
        try {

          if (wwwminiapcnIpString!=null) {
            wwwminiapcnIp = InetAddress.getByAddress(ipString2IpBytes(wwwminiapcnIpString));
          }
          if (dnsminiapcnIpString!=null) {
            dnsminiapcnIp = InetAddress.getByAddress(ipString2IpBytes(dnsminiapcnIpString));
          }
        } catch (UnknownHostException e) {
          logger.error("DNS inited error:" + e.getMessage(), e);
        }
      }



      @Override
      public InetAddress[] resolve(final String host) throws UnknownHostException {


        if ( !ebNet ) {
          return super.resolve(host);
        }

        if (host.equalsIgnoreCase("www.miniap.cn") && wwwminiapcnIp!=null) {
          return new InetAddress[] { wwwminiapcnIp };
        } else if (host.equalsIgnoreCase("dns.miniap.cn") && dnsminiapcnIp!=null) {
          return new InetAddress[] { dnsminiapcnIp };
        } else {
          return super.resolve(host);
        }


      }

    };*/

        // Create a connection manager with custom configuration.
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, null, null, null, connTimeToLive, TimeUnit.MILLISECONDS);

        //connManager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(maxTotalThread);
        manager.setDefaultMaxPerRoute(maxTotalThread); //very important.


        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();

        // Configure the connection manager to use socket configuration either
        // by default.
        manager.setDefaultSocketConfig(socketConfig);


        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(6000)
                .build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setBufferSize(bufferSize)
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        // Configure the connection manager to use connection configuration either
        // by default.
        manager.setDefaultConnectionConfig(connectionConfig);
        return manager;
    }

    public static byte[] ipString2IpBytes(String ipString) {
        Matcher m = ipStringPattern.matcher(ipString);
        byte[] ipByte = new byte[4];
        if (m.find()) {
            for (int i = 0; i < ipByte.length; i++) {
                ipByte[i] = (byte) Integer.parseInt(m.group(i + 1));
            }
        }
        return ipByte;
    }


    /**
     *
     * @param cookieStore
     * @return
     */
    public static CloseableHttpClient getClient(CookieStore cookieStore){
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60000)
                .setSocketTimeout(0)
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .build();

        PoolingHttpClientConnectionManager manager = getConnectManager(64 * 1024, 1024 * 2, 2 * 3600l);
        return HttpClients.custom()
                .setConnectionManager(manager)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore)
                .build();

    }
    public static CloseableHttpResponse executeHttpGet(String url, CloseableHttpClient client, String proxy) throws IOException {
        HttpGet get = new HttpGet(url);
        if (proxy != null) {
            String[] tmp = proxy.split(":");
            if (tmp.length == 2) {
                HttpHost httpHost = new HttpHost(tmp[0], Integer.parseInt(tmp[1]));
                RequestConfig config = RequestConfig.custom()
                        .setProxy(httpHost)
                        .setConnectTimeout(30000)
                        .setConnectionRequestTimeout(30000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .setCircularRedirectsAllowed(false)
                        .setCookieSpec(CookieSpecs.BEST_MATCH)
                        .build();
                get.setConfig(config);
            }
        }
        return client.execute(get);
    }

    /**
     *
     * @param url
     * @param client
     * @param proxy
     * @return
     * @throws IOException
     */
    public static String getResponse(String url, CloseableHttpClient client, String proxy) throws IOException {
        HttpGet get = new HttpGet(url);
        if (proxy != null) {
            String[] tmp = proxy.split(":");
            if (tmp.length == 2) {
                HttpHost httpHost = new HttpHost(tmp[0], Integer.parseInt(tmp[1]));
                RequestConfig config = RequestConfig.custom()
                        .setProxy(httpHost)
                        .setConnectTimeout(10000)
                        .setConnectionRequestTimeout(10000)
                        .setSocketTimeout(10000)
                        .setRedirectsEnabled(false)
                        .setCircularRedirectsAllowed(false)
//                        .setCookieSpec(CookieSpecs.BEST_MATCH)
                        .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                        .build();
                get.setConfig(config);
            }
        }
        CloseableHttpResponse response =  client.execute(get);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity, "UTF-8");
        }else{
            return "";
        }
    }

    public static String getResponseString(CloseableHttpResponse response,String entityCharset) throws IOException{
        String result;
        HttpEntity entity = response.getEntity();
        /*
        if (httpEntity != null) {
                // 打印响应内容
                System.out.println("response:"
                        + EntityUtils.toString(httpEntity, "UTF-8"));
            }
            // 释放资源
            closeableHttpClient.close();
         */
        if (entity != null) {
            if (entityCharset == null) {
                result = EntityUtils.toString(entity);
            } else {
                result = EntityUtils.toString(entity, entityCharset);
            }
            return result;
        } else {
            return "";
        }
    }
}
