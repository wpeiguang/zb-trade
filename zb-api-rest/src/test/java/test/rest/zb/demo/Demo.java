package test.rest.zb.demo;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import entity.commons.UserApi;
import entity.enumtype.ExchangeEnum;
import entity.enumtype.KTimeEnum;
import entity.exchange.DeptResult;
import entity.exchange.DeptResult.Dept;
import entity.exchange.KlineResult;
import jodd.props.Props;
import rest.zb.rest.api.RestApiGet;
import rest.zb.rest.api.RestApiPost;
import rest.zb.rest.entity.Order;
import rest.zb.rest.entity.TradeResult;
import rest.zb.rest.entity.post.Account;

public class Demo {
	private RestApiPost apiPost;
	private RestApiGet apiGet;
	private String apiKey = "401524ae-360a-43e3-b445-e8e270e73254";
	private String secretKey = "23e9ac55-290e-4a8d-bb2a-d44ca09357a7";
	private Map<String, Double> baseCoinPrice = new HashMap<>();

	//@Test
	public void 行情api() {
		String result = apiGet.getAllTicker();
		System.out.println(result);
//		statistics(result);
		//获取k线图
//		List<KlineResult> kline = apiGet.getKline(KTimeEnum.min1);
//		KlineResult klineResult = kline.get(kline.size() - 1);
//		System.out.println(klineResult.getDate()+" "+klineResult);
		
//		获取买卖方所有深度
//		DeptResult dept = apiGet.getDept();
//		//获取买方深度
//		List<Dept> asks = dept.getAsks();
//		//卖1详情
//		Dept ask1 = asks.get(0);
//		System.out.println("卖1,价:"+ask1.getPrice()+",量:"+ask1.getAmount());
//		List<Dept> bids = dept.getBids();
//		Dept bid1 = bids.get(0);
//		System.out.println("买1,价:"+bid1.getPrice()+",量:"+bid1.getAmount());
	}

	private void statistics(String tickerJson) throws Exception{
		JSONObject jsonObject = new JSONObject(tickerJson);
		Iterator<String> keys = jsonObject.keys();
		List<String> coins = new ArrayList<>();
		while (keys.hasNext()){
			String market = keys.next();
			if(market.contains("qc")) {
				coins.add(market.substring(0, market.length()-2));
			}
		}
		coins.remove("bitcny");
		coins.remove("usdt");

		JSONObject usdt = jsonObject.getJSONObject("usdtqc");
		JSONObject zb = jsonObject.getJSONObject("zbqc");
		JSONObject btc = jsonObject.getJSONObject("btcqc");
		double usdtBuy = Double.valueOf(usdt.getString("buy"));
		double usdtSell = Double.valueOf(usdt.getString("sell"));
		double zbBuy = Double.valueOf(zb.getString("buy"));
		double zbSell = Double.valueOf(zb.getString("sell"));
		double btcBuy = Double.valueOf(btc.getString("buy"));
		double btcSell = Double.valueOf(btc.getString("sell"));
		baseCoinPrice.put("usdt_buy", usdtBuy);
		baseCoinPrice.put("usdt_sell", usdtSell);
		baseCoinPrice.put("zb_buy", zbBuy);
		baseCoinPrice.put("zb_sell", zbSell);
		baseCoinPrice.put("btc_buy", btcBuy);
		baseCoinPrice.put("btc_sell", btcSell);
		String actBuyPrice = "0";
		String actSellPrice = "0";

		for (int i = 0; i < coins.size(); i++) {
			String coin = coins.get(i);
			double maxBuyPrice = 0;
			double minSellPrice = Double.MAX_VALUE;
			String buyMarket = "";
			String sellMarket = "";
			double buy1Price = 0;
			double sell1Price = 0;
			JSONObject market;
			//usdt市场
			String tradeMarket = coin+"usdt";
			String marketStr = coin+"_usdt";
			if(jsonObject.has(tradeMarket)) {
				market = jsonObject.getJSONObject(tradeMarket);
				buy1Price = Double.valueOf(market.getString("buy")) * usdtBuy;
				sell1Price = Double.valueOf(market.getString("sell")) * usdtSell;
				if (buy1Price > maxBuyPrice) {
					maxBuyPrice = buy1Price;
					sellMarket = marketStr;
					actSellPrice = market.getString("buy");
				}
				if (sell1Price < minSellPrice) {
					minSellPrice = sell1Price;
					buyMarket = marketStr;
					actBuyPrice = market.getString("sell");
				}
			}
			//zb市场
			tradeMarket = coin+"zb";
			marketStr = coin+"_zb";
			if(jsonObject.has(tradeMarket)) {
				market = jsonObject.getJSONObject(tradeMarket);
				buy1Price = Double.valueOf(market.getString("buy")) * zbBuy;
				sell1Price = Double.valueOf(market.getString("sell")) * zbSell;
				if (buy1Price > maxBuyPrice) {
					maxBuyPrice = buy1Price;
					sellMarket = marketStr;
					actSellPrice = market.getString("buy");
				}
				if (sell1Price < minSellPrice) {
					minSellPrice = sell1Price;
					buyMarket = marketStr;
					actBuyPrice = market.getString("sell");
				}
			}

			//btc市场
			tradeMarket = coin+"btc";
			marketStr = coin+"_btc";
			if(jsonObject.has(tradeMarket)) {
				market = jsonObject.getJSONObject(tradeMarket);
				buy1Price = Double.valueOf(market.getString("buy")) * btcBuy;
				sell1Price = Double.valueOf(market.getString("sell")) * btcSell;
				if (buy1Price > maxBuyPrice) {
					maxBuyPrice = buy1Price;
					sellMarket = marketStr;
					actSellPrice = market.getString("buy");
				}
				if (sell1Price < minSellPrice) {
					minSellPrice = sell1Price;
					buyMarket = marketStr;
					actBuyPrice = market.getString("sell");
				}
			}

			//qc市场
			tradeMarket = coin+"qc";
			marketStr = coin+"_qc";
			if(jsonObject.has(tradeMarket)) {
				market = jsonObject.getJSONObject(tradeMarket);
				buy1Price = Double.valueOf(market.getString("buy"));
				sell1Price = Double.valueOf(market.getString("sell"));
				if (buy1Price > maxBuyPrice) {
					maxBuyPrice = buy1Price;
					sellMarket = marketStr;
					actSellPrice = market.getString("buy");
				}
				if (sell1Price < minSellPrice) {
					minSellPrice = sell1Price;
					buyMarket = marketStr;
					actBuyPrice = market.getString("sell");
				}
			}

			if(maxBuyPrice > minSellPrice) {
				double apr = (maxBuyPrice - minSellPrice) / minSellPrice * 100;
//				if (apr > 1) {
					System.out.println(coin + ": 买入交易对：" + buyMarket + ", 卖出交易对：" + sellMarket + ", 买价：" + minSellPrice + "/" + actBuyPrice + ", 卖价：" + maxBuyPrice + "/" + actSellPrice + ", 价格差百分比：" + apr);
//					System.out.println(coin + ", 买价：" + minSellPrice + "/" + actBuyPrice + ", 卖价：" + maxBuyPrice + "/" + actSellPrice + ", 价格差百分比：" + apr);
//					trade(buyMarket, sellMarket, marketStr.split("_")[1], minSellPrice, maxBuyPrice);
//				}
			}
		}
	}

	private void trade(String buyMarket, String sellMarket, String baseCoin, double buy, double sell){
		RestApiGet buyApiGet = new RestApiGet(buyMarket);
		RestApiPost buyApiPost = new RestApiPost(buyMarket, new UserApi(ExchangeEnum.zb, "15112556673", apiKey, secretKey));
		double buyPrice = buyApiGet.getDept().getAsks().get(0).getPrice();
		double buyAmount = buyApiGet.getDept().getAsks().get(0).getAmount();
		double sellPrice = buyApiGet.getDept().getAsks().get(0).getPrice();
		double sellAmount = buyApiGet.getDept().getAsks().get(0).getAmount();
		if(buy != buyPrice || sell != sellPrice){
			return;
		}

		RestApiPost sellApiPost = new RestApiPost(sellMarket, new UserApi(ExchangeEnum.zb, "15112556673", apiKey, secretKey));

		double amount = buyAmount > sellAmount ? sellAmount : buyAmount;

		double needBaseCoin = amount * buyPrice;
		Account account = apiPost.getAccount();
		double available = account.getResult().getCoin("qc").getAvailable();
		if(!baseCoin.equals("qc")){

			double baseBuyPrice = baseCoinPrice.get(baseCoin+"_buy");
			double maxCanBuyAmount = available / baseBuyPrice;
			amount = amount < maxCanBuyAmount ? amount : maxCanBuyAmount;
			needBaseCoin = amount * buyPrice;
			TradeResult buyResult = buyApiPost.buy(baseBuyPrice, needBaseCoin);
			long id = buyResult.getId();//返回挂单id
			System.out.println("买单返回结果:"+buyResult+",挂单Id:"+id);
		}

		TradeResult buyResult = buyApiPost.buy(buyPrice, amount);
		long id = buyResult.getId();//返回挂单id
		System.out.println("买单返回结果:"+buyResult+",挂单Id:"+id);

		TradeResult sellResult = sellApiPost.sell(sellPrice, amount);
		id = sellResult.getId();
		System.out.println("卖单返回结果:"+sellResult+",挂单Id:"+id);
	}

	@Test
	public void run(){
		String result = apiGet.getAllTicker();
		try {
			statistics(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//遍历用户信息
//		apiPost = new RestApiPost("btc_usd", new UserApi(ExchangeEnum.zb, "15112556673", apiKey, secretKey));
//		Account account = apiPost.getAccount();
//		account.getResult().getCoins().forEach(coin->{
//			System.out.println("可用:"+coin.getAvailable()+",冻结:"+coin.getFreez());
//		});
	}
	
	//@Test
	public void 交易api() {
		System.out.println("行情api针对的交易对:"+apiPost.getSymbol());
		//挂单
		double price = apiGet.getDept().getBids().get(2).getPrice();//获取买4价格挂单
		TradeResult buyResult = apiPost.buy(price, 1);
		long id = buyResult.getId();//返回挂单id
		System.out.println("下单返回结果:"+buyResult+",挂单Id:"+id);
		//取消订单
		TradeResult cancelResult = apiPost.cancelOrder(id);
		System.out.println("取消订单结果:"+cancelResult);
		//查询订单id详情
		Order order = apiPost.getOrder(id);
		System.out.println("订单id:"+order.getId()+",详情:"+order);
		//查询未成交订单
		List<Order> orders = apiPost.getUnfinishedOrdersIgnoreTradeType(1);
		orders.forEach(o->{
			System.out.println("未成交订单:"+o);
		});
		
		//遍历用户信息
		Account account = apiPost.getAccount();
		account.getResult().getCoins().forEach(coin->{
			System.out.println("可用:"+coin.getAvailable()+",冻结:"+coin.getFreez());
		});
	}

	@Before
	public void init() throws IOException{
		String symbol = "ltc_usdt";
		//构造行情api对象
		apiGet = new RestApiGet(symbol);
		
//		Props p = new Props();
//		p.load(new File("c:/config/zb.txt"));
//		String apiKey = p.getValue("user.zb.apikey");//修改为自己的公钥
//		String secretKey= p.getValue("user.zb.secretKey");//修改为自己的私钥
//		String apiKey = "401524ae-360a-43e3-b445-e8e270e73254";
//		String secretKey = "23e9ac55-290e-4a8d-bb2a-d44ca09357a7";
		//构造交易接口对象
//		apiPost = new RestApiPost(symbol, new UserApi(ExchangeEnum.zb, "15112556673", apiKey, secretKey));
	}
}
