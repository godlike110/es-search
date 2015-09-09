package com.md.search.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	public static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	/**
	 * 发送HttpPost请求
	 * 
	 * @param strURL
	 *            服务地址
	 * @param params
	 *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
	 * @return 成功:返回json字符串<br/>
	 */
	public static String post(String strURL, String params) {
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "text/plain"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8"); // 设置发送数据的格式
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(params);
			out.write(params);
			out.flush();
			out.close();
			// 读取响应
			int length = (int) connection.getContentLength();// 获取长度
			InputStream is = connection.getInputStream();
			if (length != -1) {
				byte[] data = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, data, destPos, readLen);
					destPos += readLen;
				}
				String result = new String(data, "UTF-8"); // utf-8编码
				return result;
			}
		} catch (IOException e) {
			logger.error("httputil error", e);
			return null;
		}
		return null; // 自定义错误信息
	}

	public static String postJSON(String strURL, String json)
			throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(strURL);
		postMethod.setRequestHeader("Accept", "text/plain, */*; q=0.01");
		postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
		postMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		postMethod.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		postMethod.setRequestBody(json);
		client.executeMethod(postMethod);
		return postMethod.getResponseBodyAsString();
	}

	public static void main(String[] args) {
		String a = "{\"query\": { \"term\": { \"name\": {\"value\": \"hello\" }}}}";
		String bString = post("http://10.51.181.25:9200/store/DATARESOURCE/2",
				a);
		System.out.println(bString);
	}

}
