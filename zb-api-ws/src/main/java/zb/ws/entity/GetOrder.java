package zb.ws.entity;

import entity.enumtype.TradeEnum;

@lombok.Data
public class GetOrder {
	private boolean success;
	private long code;
	private Data data;
	private String channel;
	private String message;
	private String no;

	@lombok.Data
	public static class Data {
		/**挂单总数量*/
		private double total_amount;
		private String id;
		private double price;
		private long trade_date;
		private int status;
		private double trade_money;
		private double trade_amount;
		/**type : 挂单类型 1/0[buy/sell]*/
		private int type;
		private double trade_price;
		private String currency;
		
		public TradeEnum getType() {
			if(type == 1) {
				return TradeEnum.buy;	
			}
			return TradeEnum.sell;
		}
	}
}