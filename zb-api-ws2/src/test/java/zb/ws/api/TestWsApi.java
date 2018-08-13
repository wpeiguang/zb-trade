package zb.ws.api;

import java.io.IOException;

import cn.hutool.core.thread.ThreadUtil;
import lombok.val;
import ws.api.ApiConfig;
import zb.entity.UserApiEntity;
import zb.enumtype.ExchangeEnum;
import zb.ws.adapter.AdapterZb;

public class TestWsApi {

	public static void main(String[] args) throws IOException {
		
		val userApi = new UserApiEntity(ExchangeEnum.zb, "测试", "","");
//		val apiConfig = new ApiConfig("ws://127.0.0.1:9326", null, 23128);
		val api = new WsZb(userApi, new AdapterZb());
		String symbol= "eth_usdt";
		while(true) {
//			api.addChannel("depth_ltcbtc");
//			api.ping();
			
			api.sendStr("{\"channel\":\"depth\",\"event\":\"addChannel\",\"params\":[{\"size\":1,\"symbol\":\"zbqc\"},{\"size\":2,\"symbol\":\"usd1tqc\"},{\"size\":51,\"symbol\":\"zbusdt\"}]}");
			//{"channel":"depth","event":"addChannel","params":[{"size":1,"symbol":"zb_qc"},{"size":2,"symbol":"usdt_qc"},{"size":3,"symbol":"zb_usdt"}]}
			//{"channel":"addChannel","event":"kline","params":[{"symbol":"zbqc","type":"1min"}]}
			
			ThreadUtil.sleep(10000);
		}
		
//		api.saveChannel("ltcusdt");
//		while(true) {
//			api.getAccount();
//			ThreadUtil.sleep(1000 * 20);
//		}
		
//		ubtcbtcdefault
//		api.buy(symbol, 650, 0.001);
//		api.getOrder(symbol, 20180522105585216l, "测试");
//		api.cancel(symbol, 20180522105585216l, "取消");
		
	}
}
