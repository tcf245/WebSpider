package crawl.spider.downloader;

import crawl.spider.Site;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import java.io.IOException;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.4.0
 */
public class HttpClientGenerator {

    private PoolingHttpClientConnectionManager connectionManager;

    public HttpClientGenerator() {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);
    }

    public HttpClientGenerator setPoolSize(int poolSize) {
        connectionManager.setMaxTotal(poolSize);
        return this;
    }

    public CloseableHttpClient getClient(Site site) {
        return generateClient(site);
    }

    private CloseableHttpClient generateClient(Site site) {
        CredentialsProvider credsProvider = null;
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        
//        if(proxy!=null && StringUtils.isNotBlank(proxy.getUser()) && StringUtils.isNotBlank(proxy.getPassword()))
//        {
//            credsProvider= new BasicCredentialsProvider();
//            credsProvider.setCredentials(
//                    new AuthScope(proxy.getHttpHost().getAddress().getHostAddress(), proxy.getHttpHost().getPort()),
//                    new UsernamePasswordCredentials(proxy.getUser(), proxy.getPassword()));
//            httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
//        }

        if(site!=null){
            credsProvider = new BasicCredentialsProvider();
            httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
        }
        
        httpClientBuilder.setConnectionManager(connectionManager);
        if (site == null) {
            httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

                public void process(
                        final HttpRequest request,
                        final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }
            });
        }


        SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        if (site != null) {
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));
        }
        generateCookie(httpClientBuilder, site);
        return httpClientBuilder.build();
    }

    private void generateCookie(HttpClientBuilder httpClientBuilder,Site site){

    }

//    private void generateCookie(HttpClientBuilder httpClientBuilder, Site site) {
//        CookieStore cookieStore = new BasicCookieStore();
//        for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
//            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
//            cookie.setDomain(site.getDomain());
//            cookieStore.addCookie(cookie);
//        }
//        for (Map.Entry<String, Map<String, String>> domainEntry : site.getAllCookies().entrySet()) {
//            for (Map.Entry<String, String> cookieEntry : domainEntry.getValue().entrySet()) {
//                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
//                cookie.setDomain(domainEntry.getKey());
//                cookieStore.addCookie(cookie);
//            }
//        }
//        httpClientBuilder.setDefaultCookieStore(cookieStore);
//    }

}
