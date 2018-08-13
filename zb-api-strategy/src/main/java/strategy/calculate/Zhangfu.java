package strategy.calculate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.exchange.KlineResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 涨幅
 * 
 * 涨幅=(现价-上一个交易日收盘价）/上一个交易日收盘价*100%
 */
@Data
@AllArgsConstructor
public class Zhangfu {
	private List<KlineResult> kline;
	
	/**
	 * 幅度
	 * 
	 * @return
	 */
	public List<Double> getFudu() {
		List<Double> list = new ArrayList<Double>();
		//计算出来是正确的
		for (int i = kline.size() - 1; i < kline.size(); i--) {
			
			if(i > 0){
				double fudu = ((kline.get(i).getClose() - kline.get(i - 1).getClose()) / kline.get(i).getClose()) * 100;
				list.add(fudu);
			}		
		}
		Collections.reverse(list);
		return list;
	}
}
