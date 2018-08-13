package test.strategy;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import entity.commons.UserApi;
import entity.enumtype.ExchangeEnum;
import entity.enumtype.KTimeEnum;
import entity.exchange.KlineResult;
import jodd.props.Props;
import rest.zb.rest.api.RestApiGet;
import rest.zb.rest.api.RestApiPost;
import rest.zb.rest.entity.TradeResult;
import strategy.calculate.Ma;
import strategy.calculate.MaEntity;
import strategy.calculate.Zhangfu;

public class StrategyDemo {

	private RestApiPost apiPost;
	private RestApiGet apiGet;
	
	@Test
	public void zhangfu() {
		List<KlineResult> kline = apiGet.getKline(KTimeEnum.min1);
		Zhangfu zhangfu = new Zhangfu(kline);
		List<Double> fudu = zhangfu.getFudu();
		Collections.reverse(fudu);//反转过来,用于方便跟页面进行对比
		System.out.println(fudu);
		
	}

//	@Test
	public void demo() {
		List<KlineResult> kline = apiGet.getKline(KTimeEnum.min1);
		MaEntity ma = new Ma(kline).getMa();
		double[] ma5 = ma.getMa5();
		double[] ma10 = ma.getMa10();
		double[] ma30 = ma.getMa30();
		double ma5Value = ma5[ma5.length - 1];
		System.out.println("最新ma5:" + ma5Value);
		double ma10Value = ma10[ma10.length - 1];
		System.out.println("最新ma10:" + ma10Value);
		if(ma5Value > ma10Value) {
			TradeResult buy = apiPost.buy(0, 0);
		}
	}

	@Before
	public void init() throws IOException {
		String symbol = "zb_qc";
		// 构造行情api对象
		apiGet = new RestApiGet(symbol);

		Props p = new Props();
		p.load(new File("c:/config/zb.txt"));
		String apiKey = p.getValue("user.zb.apikey");// 修改为自己的公钥
		String secretKey = p.getValue("user.zb.secretKey");// 修改为自己的私钥
		// 构造交易接口对象
		apiPost = new RestApiPost(symbol, new UserApi(ExchangeEnum.zb, "测试", apiKey, secretKey));
	}
}
