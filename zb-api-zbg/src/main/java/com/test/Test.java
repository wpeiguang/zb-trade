package com.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import jodd.props.Props;

public class Test {
	public static void main(String[] args) throws IOException {
		String url = "https://www.zbg.com/exchange/fund/controller/website/fundcontroller/findByPage";

		HttpClient httpClient = new HttpClient();
		PostMethod post = new PostMethod(url);

		HttpMethodParams params = new HttpMethodParams();
		params.setContentCharset("UTF-8");
		post.setParams(params);

		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		String bodyStr = "{\"pageIndex\":\"1\", \"pageSize\":\"10\"}";

		Props p = new Props();
		p.load(new File("c:/config/zbg.txt"));
		String apiKey = p.getValue("akey");// 修改为自己的公钥
		String secretKey = p.getValue("skey");// 修改为自己的私钥

		Map<String, String> headers = SignUtils.getHeaderOfBodyJson(apiKey, secretKey, bodyStr);

		Set<String> set = headers.keySet();
		for (String key : set) {
//			httpPost.setHeader(key, headers.get(key));
			post.setRequestHeader(key, headers.get(key));
		}
		try {
			RequestEntity entity = new StringRequestEntity(bodyStr, "application/json; charset=utf-8", "utf-8");
			post.setRequestEntity(entity);
			httpClient.executeMethod(post);
			String html = post.getResponseBodyAsString();
			System.out.println("结果:" + html);
		} catch (IOException e) {
			System.out.println("异常");
		}
	}

}
