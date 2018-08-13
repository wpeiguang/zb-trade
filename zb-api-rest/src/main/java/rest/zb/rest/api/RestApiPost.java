
package rest.zb.rest.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpException;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import entity.commons.UserApi;
import entity.enumtype.SymbolEnum;
import entity.enumtype.TradeEnum;
import jodd.util.StringUtil;
import kits.my.EncryDigestUtil;
import kits.my.HttpUtilManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import rest.zb.api.util.MapSort;
import rest.zb.rest.entity.Order;
import rest.zb.rest.entity.TradeResult;
import rest.zb.rest.entity.post.Account;

@Data
@Slf4j
public class RestApiPost {
	private String symbol;
	private UserApi userApi;

	public static String url = "https://trade.zb.com/api/";

	public RestApiPost(SymbolEnum symbol, UserApi userApi) {
		super();
		this.userApi = userApi;
		this.symbol = symbol.name();
	}

	public RestApiPost(String symbol, UserApi userApi) {
		super();
		this.userApi = userApi;
		this.symbol = symbol;
	}

	/** 现价买入 */
	public TradeResult buy(double price, double amount) {
		
		TimeInterval timer = DateUtil.timer();
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("method", "order");
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面

		params.put("price", Convert.toStr(price));
		params.put("amount",Convert.toStr(amount));
		params.put("tradeType", "1");// 交易类型1/0[buy/sell]
		params.put("currency", symbol);// 交易类型(目前仅支持BTC/LTC/ETH)
		log.debug("下单参数:" + params.toString());
		String json = getJsonPost(params);
		log.debug("执行时间:"+timer.intervalMs()+"下单返回:" + json);
		return JSONObject.parseObject(json, TradeResult.class);
	}

	/** 现价卖出 */
	public TradeResult sell(double price, double amount) {
		TimeInterval timer = DateUtil.timer();
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("method", "order");
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面

		params.put("price", Convert.toStr(price));
		params.put("amount", Convert.toStr(amount));
		params.put("tradeType", "0");// 交易类型1/0[buy/sell]
		params.put("currency", symbol);// 交易类型(目前仅支持BTC/LTC/ETH)
		log.debug("下单参数:" + params.toString());
		String json = getJsonPost(params);
		log.debug("执行时间:"+timer.intervalMs()+"下单返回:" + json);
		return JSONObject.parseObject(json, TradeResult.class);
	}

	/** 获取订单信息 **/
	public Order getOrder(long orderId) {
		TimeInterval timer = DateUtil.timer();
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey,3.id,4.currency
		params.put("method", "getOrder");
		params.put("accesskey", userApi.getApiKey());//
		// 这个需要加入签名,放前面
		params.put("id", orderId + "");
		params.put("currency", symbol);// 默认
		String json = this.getJsonPost(params);

		log.debug("执行时间:"+timer.intervalMs()+"查询订单:" + json);
		return JSONObject.parseObject(json, Order.class);
		
	}

	/**
	 * 获取所有订单,获取多个委托买单或卖单，每次请求返回10条记录(返回null则没有订单)
	 *
	 * @param tradeType交易类型1/0[buy/sell]
	 * @param pageIndex页数
	 */
	public void getOrders(TradeEnum tradeType, int pageIndex) {
		TimeInterval timer = DateUtil.timer();
		// 会抛出json{"code":3001,"message":"挂单没有找到"}
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey,3.id,4.currency
		params.put("method", "getOrders");
		params.put("accesskey", userApi.getApiKey());//
		// 这个需要加入签名,放前面
		params.put("tradeType", Convert.toStr(tradeType.getType()));
		params.put("currency", symbol);
		params.put("pageIndex", pageIndex + "");
		// log.debug(params.toString());
		String json = getJsonPost(params);
		log.debug("执行时间:"+timer.intervalMs()+"获取多个委托:" + json);
	}

	//
	//
	// private List<ChbtcOrder> convertOrder(String json) {
	// // 重新拆分json,重新组合对象
	// json = StringUtil.substring(json, 0, json.length() - 2);
	// json = StringUtil.substring(json, 2, json.length());
	// String[] split = StringUtil.split(json, "},{");
	// List<ChbtcOrder> list = new ArrayList<ChbtcOrder>();
	// ChbtcOrder o = null;
	// for (String s : split) {
	// try {
	// o = JSON.parseObject("{" + s + "}", ChbtcOrder.class);
	// list.add(o);
	// } catch (Exception e) {
	//// log.info("解析错误的json:"+s);
	// }
	// }
	// return list;
	// }
	//
	// public List<ChbtcOrder> getOrdersIgnoreTradeType(int pageIndex) {
	// String cacheName = cName+"getOrdersIgnoreTradeType";
	// List<ChbtcOrder> list = CacheKit.get(cache1s, cacheName);
	// if(list != null){
	// return list;
	// }
	// // 会抛出json{"code":3001,"message":"挂单没有找到"}
	// Map<String, String> params = new LinkedHashMap<String, String>();
	// // 保持队形1.method,2.accesskey,3.id,4.currency
	// params.put("method", "getOrdersIgnoreTradeType");
	// params.put("accesskey", userApi.getApiKey());//
	// // 这个需要加入签名,放前面
	//// params.put("tradeType", "1");// 交易类型1/0[buy/sell]
	// params.put("currency", symbol.name());
	// params.put("pageIndex", pageIndex + "");
	// params.put("pageSize", "100");// 每页数量
	//
	// String json = getJsonPost(params);
	//
	// list = convertOrder(json);
	// CacheKit.put(cache1s, cacheName,list);
	// return list;
	// }
	//
	/**
	 * 获取未成交或部份成交的买单和卖单，每次请求返回pageSize<=100条记录
	 *
	 * @param pageIndex
	 * @return
	 */
	public List<Order> getUnfinishedOrdersIgnoreTradeType(int pageIndex) {
		// 会抛出json{"code":3001,"message":"挂单没有找到"}
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey,3.id,4.currency
		params.put("method", "getUnfinishedOrdersIgnoreTradeType");
		params.put("accesskey", userApi.getApiKey());//
		// 这个需要加入签名,放前面
		params.put("currency", symbol);
		params.put("pageIndex", Convert.toStr(pageIndex));
		params.put("pageSize", "10");// 每页数量
		log.debug("参数:" + params);
		String json = getJsonPost(params);
		log.debug("未成交订单:" + json);
		try{
			return JSONObject.parseArray(json, Order.class);
		}catch(Exception e){
			
		}
		return new ArrayList<Order>();
	}

	/** 取消订单 **/
	public TradeResult cancelOrder(long orderId) {
		TimeInterval timer = DateUtil.timer();
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey,3.id,4.currency
		params.put("method", "cancelOrder");
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
		params.put("id", orderId + "");
		params.put("currency", symbol);// 默认
		String json = getJsonPost(params);
		log.debug("执行时间:"+timer.intervalMs()+"取消:" + json);
		return JSONObject.parseObject(json, TradeResult.class);
	}

	/** 获取用户信息 **/
	public Account getAccount() {
		TimeInterval timer = DateUtil.timer();
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey
		params.put("method", "getAccountInfo");
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
		log.debug("参数:" + params);
		String json = this.getJsonPost(params);
		log.debug("执行时间:"+timer.intervalMs()+"用户信息:" + json);

		return JSONObject.parseObject(json, Account.class);

	}

	/** 获取用户充值地址 */
	public String getUserAddress() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey
		params.put("method", "getUserAddress");
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
		params.put("currency", symbol.substring(0, symbol.indexOf("_")));// 默认,需要去掉_cny后缀
		
		String json = this.getJsonPost(params);
		return json;
	}

	/** 获取用户认证的提现地址 */
	public String getWithdrawAddress() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey
		params.put("method", "getWithdrawAddress");
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
		params.put("currency", symbol.substring(0, symbol.indexOf("_")));

		String json = this.getJsonPost(params);
		return json;
	}

	// /**提现*/
	//// 提现矿工费
	//// 比特币最低是0.0003
	//// 莱特币最低是0.001
	//// 以太币最低是0.01
	//// ETC最低是0.01
	//// BTS最低是3
	//// EOS最低是1
	public String withdraw(double amount, double fees, String receiveAddr) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey

		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
		params.put("amount", Convert.toStr(amount));// 提现金额
		params.put("currency", symbol.substring(0, symbol.indexOf("_")));
		params.put("fees", Convert.toStr(fees));
		params.put("itransfer", "0");// 是否同意bitbank系内部转账(0不同意，1同意，默认不同意)
		params.put("method", "withdraw");
		params.put("receiveAddr", receiveAddr);// 接收地址（必须是认证了的地址，bts的话，以"账户_备注"这样的格式）
		params.put("safePwd", userApi.getPayPass());
		
		String json = this.getJsonPost(params);
		return json;
	}

	//
	/** 获取数字资产提现记录 */
	public String getWithdrawRecord() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey
		params.put("method", "getWithdrawRecord");
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
		params.put("currency", symbol.substring(0, symbol.indexOf("_")));
		params.put("pageIndex", "1");
		params.put("pageSize", "10");
		String json = this.getJsonPost(params);
		return json;
	}

	/** 获取数字资产充值记录 */
	public String getChargeRecord() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		// 保持队形1.method,2.accesskey
		params.put("method", "getChargeRecord");
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
		params.put("currency", symbol.substring(0, symbol.indexOf("_")));
		params.put("pageIndex", "1");
		params.put("pageSize", "10");
		String json = this.getJsonPost(params);
		return json;
	}
	// /**获取人民币提现记录*/
	// public String getCnyWithdrawRecord(){
	// Map<String, String> params = new LinkedHashMap<String, String>();
	// // 保持队形1.method,2.accesskey
	// params.put("method", "getCnyWithdrawRecord");
	// params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
	// params.put("pageIndex", "1");
	// params.put("pageSize", "10");
	// log.debug(params);
	// String json = this.getJsonPost(params);
	// return json;
	// }
	// /**获取人民币充值记录*/
	// public String getCnyChargeRecord(){
	// Map<String, String> params = new LinkedHashMap<String, String>();
	// // 保持队形1.method,2.accesskey
	// params.put("method", "getCnyChargeRecord");
	// params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
	// params.put("pageIndex", "1");
	// params.put("pageSize", "10");
	// log.debug(params);
	// String json = this.getJsonPost(params);
	// return json;
	// }

	/**
	 * 获取json内容
	 * 
	 * @param params
	 * @return
	 */
	private String getJsonPost(Map<String, String> params) {
		params.put("accesskey", userApi.getApiKey());// 这个需要加入签名,放前面
		// String paramsForStr = getParamsForStr(params);// 将参数字符串化
		String digest = EncryDigestUtil.digest(userApi.getSecretKey());

		// log.debug("SecretKey digest加密后:"+digest);
		// log.debug("要md5加密的字符串:"+paramsForStr);
		// String sign = EncryDigestUtil.hmacSign(paramsForStr, digest);
		String sign = EncryDigestUtil.hmacSign(toStringMap(params), digest);
		
		// log.debug("得到的sign:"+sign);
		String method = params.get("method");

		// 加入验证
		params.put("sign", sign);
		params.put("reqTime", Convert.toStr(System.currentTimeMillis()));
		
//		System.out.println("aa:"+toStringMap(params));
		// log.debug(Convert.toString(System.currentTimeMillis()));
		// HttpRequest request = HttpRequest.get(url + method).query(params);
		// log.debug(request.url());
		// String json = request.send().bodyText();
		String json = "";
		try {
			json = HttpUtilManager.getInstance().requestHttpPost(url, method, params);
			log.info(symbol+"函数:"+method+",返回:"+json);
		} catch (HttpException | IOException e) {
			log.error("获取交易json异常", e);
		}
		return json;
	}

	public static void main(String[] args) {
		String params = "method=order&accesskey=f5f08725-6a14-4f95-a9cb-25f2299dff2f&price=10&amount=0.01&tradeType=1&currency=etc_cny";
		String secretkey = "1be5ac67-94ce-4f8c-8c47-e73ed5242f25";
		String sign = EncryDigestUtil.hmacSign(params, EncryDigestUtil.digest(secretkey));
		log.debug(sign);
	}

	/**
	 * 将参数集转为字符串
	 * 
	 * @param params
	 * @return
	 */
	private String getParamsForStr(Map<String, String> params) {
		Iterator<String> it = params.keySet().iterator();
		String str = "";
		while (it.hasNext()) {
			String key = it.next();
			String value = params.get(key);
			str += key + "=" + value + "&";
		}
		str = StringUtil.substring(str, 0, str.length() - 1);
		return str;
	}

	public String toStringMap(Map m) {
		// 按map键首字母顺序进行排序
		m = MapSort.sortMapByKey(m);

		StringBuilder sbl = new StringBuilder();
		for (Iterator<Entry> i = m.entrySet().iterator(); i.hasNext();) {
			Entry e = i.next();
			Object o = e.getValue();
			String v = "";
			if (o == null) {
				v = "";
			} else if (o instanceof String[]) {
				String[] s = (String[]) o;
				if (s.length > 0) {
					v = s[0];
				}
			} else {
				v = o.toString();
			}
			if (!e.getKey().equals("sign") && !e.getKey().equals("reqTime") && !e.getKey().equals("tx")) {
				// try {
				// sbl.append("&").append(e.getKey()).append("=").append(URLEncoder.encode(v,
				// "utf-8"));
				// } catch (UnsupportedEncodingException e1) {
				// e1.printStackTrace();
				sbl.append("&").append(e.getKey()).append("=").append(v);
				// }
			}
		}
		String s = sbl.toString();
		if (s.length() > 0) {
			return s.substring(1);
		}
		return "";
	}
}
