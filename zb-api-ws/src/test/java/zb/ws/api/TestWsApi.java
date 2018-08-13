package zb.ws.api;

import java.io.IOException;

import lombok.val;
import ws.api.ApiConfig;
import zb.entity.UserApiEntity;
import zb.enumtype.ExchangeEnum;
import zb.ws.adapter.AdapterZb;

public class TestWsApi {

	public static void main(String[] args) throws IOException {
		
		val userApi = new UserApiEntity(ExchangeEnum.zb, "15112556673", "401524ae-360a-43e3-b445-e8e270e73254","23e9ac55-290e-4a8d-bb2a-d44ca09357a7");
//		val apiConfig = new ApiConfig("wss://api.zb.com:9999/websocket", "192.168.5.182", 23128);
		val api = new WsZb(userApi, new AdapterZb());
		String symbol= "hpyusdt";
//		api.depth(symbol);
//		api.saveChannel("zbqc_depth");
//		api.saveChannel("ltcbtc_trades");
//		api.getAccount();
//		api.sell(symbol,6.99, 0.00508);
		api.buy(symbol, 0.00508, 100);
//		api.getOrder(symbol, 20180522105585216l, "测试");
//		api.cancel(symbol, 20180522105585216l, "取消");
	}
}
