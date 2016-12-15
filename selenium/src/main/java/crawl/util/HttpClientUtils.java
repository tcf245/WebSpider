package crawl.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/********************************
 * ---------------------        *
 * < HttpClientUtils.java >        *
 * ---------------------        *
 *         \   ^__^             *
 *          \  (**)\__$__$__    *
 *             (__)\       )\/\ *
 *              U  ||------|    *
 *                 ||     ||    *
 ********************************/
public class HttpClientUtils {
	private static final HttpClientBuilder httpBuilder = HttpClientBuilder.create();
	private static HttpClient client = httpBuilder.build();

	private static final String ua = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36";
	public static void main(String[] args) throws Exception {
		// 链接
		String url = "http://www.gbtags.com/gb/mobileshare/4751.htm";

		// 请求头信息
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		// post参数
		Map<String,String> posts = new HashMap<String, String>();
		// posts.put("ids","430017");
		// get请求
//		String content = httpGet(url, "utf-8", headers, null);
		// post请求
//		String content = httpPost(url, "utf-8", headers, posts,null);

//		System.out.println(content);
	}

	/**
	 * get 请求示例代码
	 * @param url
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String httpGet(String url,Map<String,String> headers,boolean useProxy) throws Exception {

//		ProxyIp.Ip ip = null;
//		if (useProxy){
//			ip = ProxyIp.getProxyIp();
//			System.out.println("Get ip : " + ip.getIp());
//			HttpHost proxy = new HttpHost(ip.getIp(),ip.getPort());
//			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
//			client = HttpClients.custom().setRoutePlanner(routePlanner).build();
//		}

		HttpGet httpget = new HttpGet(url);

		if (headers != null && headers.size() > 0) {
			for (String key : headers.keySet()) {
				httpget.setHeader(key, headers.get(key));
			}
		}
		HttpResponse response = client.execute(httpget);
//		if (ip != null){
//			ProxyIp.returnProxyIp(ip);
//		}

//		int code = response.getStatusLine().getStatusCode();
//		if ((int)(code/100) == 4){
//			return null;
//		}

		HttpEntity en = response.getEntity();
		return EntityUtils.toString(en);
	}

	public static void httpGetImg(String url, Map<String,String> headers,File f) throws Exception {
//		HttpClientBuilder httpBuilder = HttpClientBuilder.create();
//		httpBuilder.setUserAgent(ua);
//
//		HttpClient client = httpBuilder.build();
		HttpGet httpget = new HttpGet(url);

		if (headers != null && headers.size() > 0) {
			for (String key : headers.keySet()) {
				httpget.setHeader(key, headers.get(key));
			}
		}
		HttpResponse response = client.execute(httpget);
		HttpEntity en = response.getEntity();
		if (en != null) {
			InputStream in = en.getContent();
			try {
				byte[] buffer = new byte[1024];
				BufferedInputStream bufferedIn = new BufferedInputStream(in);
				int len = 0;
				FileOutputStream fileOutStream = new FileOutputStream(f);
				BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOutStream);
				while ((len = bufferedIn.read(buffer, 0, 1024)) != -1) {
					bufferedOut.write(buffer, 0, len);
				}
				bufferedOut.flush();
				bufferedOut.close();
			} catch (IOException ex) {
				throw ex;
			} finally {
				in.close();
			}
		}
	}

	/**
	 *  post 请求示例代码
	 * @param url
	 * @param charset
	 * @param headers
	 * @param posts
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url, String charset,Map<String,String> headers,Map<String,String> posts,String ip) throws Exception {
//		HttpClientBuilder httpBuilder = HttpClientBuilder.create();
//		httpBuilder.setUserAgent(ua);
//		HttpClient client = httpBuilder.build();
		HttpPost httppost = new HttpPost(url);

		if (null != ip && !"".equals(ip)) {
			RequestConfig requestConfig = getRequestConfigByIp(ip);
			if (null != requestConfig) {
				httppost.setConfig(requestConfig);
			}
		}

		if (headers != null && headers.size() > 0) {
			for (String key : headers.keySet()) {
				httppost.setHeader(key, headers.get(key));
			}
		}

		List<NameValuePair> postList = new ArrayList<NameValuePair>();
		if (posts != null && posts.size() > 0) {
			for (String key : posts.keySet()) {
				postList.add(new BasicNameValuePair(key,posts.get(key)));
			}
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(postList);
			httppost.setEntity(postEntity);
		}

		HttpResponse response = client.execute(httppost);
		HttpEntity en = response.getEntity();
		String content = EntityUtils.toString(en, (charset == null|| "".equals(charset)) ? "utf8" : charset);
		return content;
	}

	/**
	 *  指定ip
	 * @param ip
	 * @return
	 */
	public static RequestConfig getRequestConfigByIp(String ip) {

		RequestConfig requestConfig = null;
		try {
			RequestConfig.Builder config_builder = RequestConfig.custom();
			InetAddress inetAddress = null;
			inetAddress = InetAddress.getByName(ip);
			config_builder.setLocalAddress(inetAddress);
			requestConfig = config_builder.build();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return requestConfig;
	}
}