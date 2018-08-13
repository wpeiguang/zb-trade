package rest.zb.rest.entity;

import lombok.Data;

@Data
public class Tickers {
	private Ticker ticker;

	@Data
	public static class Ticker {
		private Double high;
		private Double low;
		private Long last;
		private Double vol;
		private Double buy;
		private Long sell;
	}
}