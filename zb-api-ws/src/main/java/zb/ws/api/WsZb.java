package zb.ws.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.neovisionaries.ws.client.ProxySettings;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import cn.hutool.core.thread.ThreadUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zb.entity.UserApiEntity;
import zb.utils.EncryDigestUtil;
import zb.ws.adapter.IAdapter;

@Data
@Slf4j
public class WsZb {
	private IAdapter iAdapter;

	private UserApiEntity userKey;

	public WsZb(UserApiEntity userKey, IAdapter iAdapter) {
		super();
		this.userKey = userKey;
		this.iAdapter = iAdapter;

		this.connect();
		do {
			ThreadUtil.sleep(1000);
			log.info("--检查握手..."+new Date());
		} while (ws == null);
	}

	private WebSocket ws;
	private boolean activite;
	private Status status;

	private Runnable mReconnectTask = new Runnable() {
		@Override
		public void run() {
			WebSocketFactory factory = new WebSocketFactory();
//			ProxySettings settings = factory.getProxySettings();
			
//			settings.setHost("192.168.5.182").setPort(23128);

			try {
//				ws = factory.createSocket("wss://api.zb.com:9999/websocket").setFrameQueueSize(5)// 设置帧队列最大值为5;
				ws = factory.createSocket("wss://api.zb.cn:9999/websocket").setFrameQueueSize(5)// 设置帧队列最大值为5;
						.setMissingCloseFrameAllowed(false);// 设置不允许服务端关闭连接却未发送关闭帧
				// .connectAsynchronously();////异步连接
				ws.addListener(new Adapter());//// 添加回调监听
				ws.connect();
				status = Status.connecting;
			} catch (IllegalArgumentException | IOException e) {
				e.printStackTrace();
			} catch (WebSocketException e) {
				e.printStackTrace();
			}
		}
	};

	private int reconnectCount = 0;// 重连次数
	private long minInterval = 3000;// 重连最小时间间隔
	private long maxInterval = 60000;// 重连最大时间间隔
	private ScheduledExecutorService executor = null;

	public void connect() {
		// 这里其实应该还有个用户是否登录了的判断 因为当连接成功后我们需要发送用户信息到服务端进行校验
		// 由于我们这里是个demo所以省略了
		if (ws != null && ws.isOpen()) {
			log.info("目前处于链接状态");
			return;
		}
		if (status == Status.connecting) {// 不是正在重连状态
			log.info("正在链接中..");
			return;
		}
		status = Status.connecting;
		reconnectCount++;

		long reconnectTime = minInterval;
		if (reconnectCount > 3) {
			long temp = minInterval * (reconnectCount - 2);
			reconnectTime = temp > maxInterval ? maxInterval : temp;
		}

		// 按指定频率周期执行某个任务。初始化延迟0ms开始执行，每隔100ms重新执行一次任务。
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(mReconnectTask, 0, reconnectTime, TimeUnit.MILLISECONDS);
	}

	private void cancelReconnect() {
		reconnectCount = 0;
		executor.shutdown();
	}

	private class Adapter extends WebSocketAdapter {
		@Override
		public void onTextMessage(WebSocket ws, String message) {
			iAdapter.onReceive(ws, message);
		}

		@Override
		public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
			super.onConnected(websocket, headers);
			log.info("连接成功");
			status = Status.connectSuccess;
			cancelReconnect();// 连接成功的时候取消重连,初始化连接次数

//			ws.sendText("{'sign': 'dd93051da4f6928c493a804235e93aab', 'amount': '300.0', 'no': '3876638', 'price': '13.57', 'tradeTyp\r\n" + 
//					"e': '1', 'channel': 'eosusdt_order', 'event': 'addChannel', 'accesskey': '03492097-bf05-487a-870a-e77b8310f10f'}");
			// ws.sendText("{'event':'addChannel','channel':'ltcusdt_ticker'}");
			// ws.sendText("{'accesskey':'ea292c13-2c28-49e7-9750-7d3ac8c9b2c1','channel':'getaccountinfo','event':'addChannel','sign':'75573f7ba77028c2bc09e6c6294d4494'}");
			// 需要重新加载
			channels.forEach(channel -> {
				ws.sendText(channel);
			});
		}

		@Override
		public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
			super.onConnectError(websocket, exception);
			log.info("连接错误");
			status = Status.connectFail;
			connect();// 连接错误的时候调用重连方法
		}

		@Override
		public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
			super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
			log.info("断开连接");
			status = Status.connectFail;
			connect();// 连接断开的时候调用重连方法
		}
	}

	// 如果重新链接需要重新订阅
	public static List<String> channels = new ArrayList<String>();

	/** 新增订阅,给行情接口使用 */
	public void saveChannel(String channel) {
		String str = "{'event':'addChannel','channel':'" + channel +"'}";//+ "_depth
		System.out.println(str);
		WebSocket sendText = ws.sendText(str);
		System.out.println(sendText.toString());
		channels.add(str);
	}
	
	public void getAccount() {
		JSONObject data = new JSONObject();
		data.put("event", "addChannel");
		data.put("channel", "getaccountinfo");
		data.put("accesskey", userKey.getApiKey());
		data.put("no", null);

		String secret = EncryDigestUtil.digest(userKey.getSecretKey());
		String sign = EncryDigestUtil.hmacSign(data.toJSONString(), secret);
		data.put("sign", sign);
		
		log.debug("获取用户请求:"+data.toJSONString());
		ws.sendText(data.toJSONString());
		
	}

	public void buy(String coin,double price, double amount){
		this.order(price, amount, coin, 1);
	}
	
	public void sell(String coin,double price, double amount){
		this.order(price, amount, coin, 0);
	}
	
	private void order(double price, double amount, String coin, int tradeType){
		JSONObject data = new JSONObject(16,true);
		data.put("accesskey", userKey.getApiKey());
		data.put("amount", amount);
		data.put("channel", coin.toLowerCase() + "_order");
		data.put("event", "addChannel");
		data.put("no", "dfddf");
		data.put("price", price);
		data.put("tradeType", tradeType);
		log.debug("要加密请求:"+data.toJSONString());
		
		String secret = EncryDigestUtil.digest(userKey.getSecretKey());
		String sign = EncryDigestUtil.hmacSign(data.toJSONString(), secret);
		data.put("sign", sign);
		
		log.debug("完整请求:"+data.toJSONString());
		ws.sendText(data.toJSONString());
	}

}
