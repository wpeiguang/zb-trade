package zb.ws.adapter;

import com.neovisionaries.ws.client.WebSocket;

public interface IAdapter {
	/**监听*/
	public void onReceive(WebSocket ws, String message);
	/**断开后重新连接*/
	public void onReconnection(WebSocket ws);
}
