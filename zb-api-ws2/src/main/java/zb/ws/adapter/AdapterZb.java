package zb.ws.adapter;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;

import lombok.extern.slf4j.Slf4j;
import zb.ws.api.WsZb;

@Slf4j
public class AdapterZb implements IAdapter {
	Gson gson = new Gson();

	@Override
	public void onReceive(WebSocket ws, String json) {
		System.out.println(json);
	}
	/**
	 * 重新连接后需要订阅
	 */
	@Override
	public void onReconnection(WebSocket ws) {
		WsZb.channels.forEach(json -> {
			log.info("重新订阅:"+json);
			ws.sendText(json);
		});
	}
}