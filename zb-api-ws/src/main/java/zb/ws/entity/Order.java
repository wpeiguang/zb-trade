package zb.ws.entity;

import lombok.Data;
/**
 * 下单结果
 */
@Data
public class Order {
	private boolean success;
	private long code;
	private Data data;
	private String channel;
	private String message;
	private String no;

	@lombok.Data
	public static class Data {
		/**下单id*/
		private long entrustId;
	}
}