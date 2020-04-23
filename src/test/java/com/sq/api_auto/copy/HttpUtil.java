package com.sq.api_auto.copy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.plaf.synth.SynthStyle;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	//存储cookie
	public static Map<String, String> cookies = new HashMap<String,String>();
	
	/**post请求
	 * @param url
	 * @param params
	 * @return 
	 * */

	public static String doPost(String url,Map<String, String> params) {
		HttpPost post = new HttpPost(url);
		//创建请求表单集合
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		//取value值
		Set<String> keys = params.keySet();
		//取value值，加到请求表单
		for (String key : keys) {
			String value = params.get(key);
			parameters.add(new BasicNameValuePair(key, value));
		}
		String result = null;//全局定义，当返回值	
		try {
			//post请求添加表单
			post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
			//发送请求,获取接口响应信息
			HttpClient client = HttpClients.createDefault();
			HttpResponse response =  client.execute(post);
			//从返回体response获取cookies
			getCookies(response);		
			//请求头添加Cookie
			addCookiesInRequesetHeaderBeforeRequest(post);
			//响应码
			//System.out.println(response.getStatusLine().getStatusCode());
			//响应主题
			result = EntityUtils.toString(response.getEntity());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	


	private static void addCookiesInRequesetHeaderBeforeRequest(HttpRequest request) {
		String sessionid = cookies.get("sessionid");
		if (sessionid!=null) {
			request.addHeader("Cookie", sessionid);
		}
	}



	/**获取cookie，存到cookies map集合中
	 * @param response
	 */
	private static void getCookies(HttpResponse response) {
		//从响应头中取出"set-cookie"
		Header setCookie = response.getFirstHeader("Set-Cookie");
		if (setCookie!=null) {
			//取出cookie键值对
			String cookiePairs = setCookie.getValue();
			if (cookiePairs!=null&&cookiePairs.trim().length()>0) {
				//以”;“切分sessionid=rh505zvq3b2a5iz1slyzwlwl9nl7mnvk; HttpOnly; Path=/
				String[] cookiePair =  cookiePairs.split(";");
				if (cookiePair!=null) {
					for (String session : cookiePair) {
						//如果包含sessionid，则意味着响应头有会话id
						if (session.contains("sessionid")) {
							cookies.put("sessionid", session);
						}
					}
				}
			}
		}
	}



	public static String doGet(String url,Map<String,String> params) {
		Set<String> keys = params.keySet();
		//定义一个标志位
		int mark = 1;
		for (String key : keys) {
			if (mark==1) {
				url+=("?"+"mobilephone"+"="+params.get(key));
			}else {
				url+=("?"+"pwd"+"="+params.get(key));
			}
			mark++;
		}
		System.out.println(url);//测试url拼接是否成功
		//指定接口提交的方式
		HttpGet get = new HttpGet(url);
		//发送请求
		HttpClient client =HttpClients.createDefault();
		HttpResponse response;
		String result = null;
		try {
			addCookiesInRequesetHeaderBeforeRequest(get);
			response = client.execute(get);
			getCookies(response);
			result = EntityUtils.toString(response.getEntity());
		} catch  (Exception e) {
		
			e.printStackTrace();
		}
		return result;
	}
	
	public static String doService(String url,String request_type,Map<String, String> params) {
		String result = null;
		if ("post".equals(request_type)) {
			result = HttpUtil.doPost(url, params);
		}else if ("get".equals(request_type)) {
			result = HttpUtil.doGet(url, params);
		}
		return result;
	}
}
