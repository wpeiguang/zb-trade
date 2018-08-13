package zb.ws.adapter;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;

import jodd.util.Wildcard;
import lombok.extern.slf4j.Slf4j;
import zb.ws.api.WsZb;
import zb.ws.entity.Account;
import zb.ws.entity.Cancel;
import zb.ws.entity.DeptResult;
import zb.ws.entity.GetOrder;
import zb.ws.entity.Order;

@Slf4j
public class AdapterZb implements IAdapter {
	Gson gson = new Gson();

	@Override
	public void onReceive(WebSocket ws, String json) {
		log.debug("zb:" + json);
		String channel = JSONObject.parseObject(json).get("channel").toString();

		if (Wildcard.matchPath(channel, "*_depth")) {
			channel = channel.substring(0, channel.lastIndexOf("_"));
			DeptResult dept = gson.fromJson(json, DeptResult.class);
			System.out.println("卖1:"+dept.getAsks().get(0).getPrice());
			// DeptUnified dept2 = un.getDept();
			// System.out.println("卖1:"+dept2.getAsks().get(0).getPrice()+",买1:"+dept2.getBids().get(0).getPrice());
		} else if ("getaccountinfo".equals(channel)) {
			Account account = gson.fromJson(json, Account.class);
			System.out.println("用户:"+account);
			// System.out.println("ltc量:"+un.getAccount().getCoins().get("ltc").getAvailable());
		}else if(Wildcard.matchPath(channel, "*_getorder")) {
			GetOrder order = gson.fromJson(json, GetOrder.class);
			System.out.println("订单:"+order);
		}else if(Wildcard.matchPath(channel, "*_cancelorder")) {
			Cancel cancel = gson.fromJson(json, Cancel.class);
			System.out.println("取消:"+cancel);
		}else if(Wildcard.matchPath(channel, "*_order")) {
			Order order = gson.fromJson(json, Order.class);
			System.out.println("下单:"+order);
		}
		
		
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