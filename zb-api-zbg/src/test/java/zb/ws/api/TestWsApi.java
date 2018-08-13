package zb.ws.api;

import java.io.IOException;

import cn.hutool.core.thread.ThreadUtil;
import lombok.val;
import zb.entity.UserApiEntity;
import zb.enumtype.ExchangeEnum;
import zb.ws.adapter.AdapterZb;

public class TestWsApi {

	public static void main(String[] args) throws IOException {
		
		
		val userApi = new UserApiEntity(ExchangeEnum.zbg);
		val api = new WsZb(userApi, new AdapterZb());
		String symbol= "eth_usdt";
		while(true) {
//			api.addChannel("depth_ltcbtc");
//			api.ping();
			
			api.sendStr("{\"channel\":\"depth_zbqc\",\"event\":\"addChannel\",\"parameters\":{\"merge\":\"0.0001\"}}");
			ThreadUtil.sleep(3000);
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
