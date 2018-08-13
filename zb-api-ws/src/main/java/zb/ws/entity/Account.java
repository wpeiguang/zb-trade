package zb.ws.entity;

import lombok.Data;
import java.util.List;

@Data
public class Account {
	private boolean success;
	private int code;
	private Data data;
	private String channel;
	private String message;
	private String no;

	@lombok.Data
	public static class Data {
		private List<Coins> coins;
		private Base base;

		@lombok.Data
		public static class Coins {
			/**币种*/
			private String key;
			private double available;
			private String freez;
			/**保留小数位 */
			private int unitDecimal;
			
			
//			private String enName;
//			private int fundstype;
//			/**币名称 例如:ZB*/
//			private String cnName;
//			private Boolean isCanRecharge;
//			private String unitTag;
//			private Boolean isCanWithdraw;
//			private Boolean canLoan;
		}

		@lombok.Data
		public static class Base {
			private String username;
			private Boolean trade_password_enabled;
			private Boolean auth_google_enabled;
			private Boolean auth_mobile_enabled;
		}
	}
}