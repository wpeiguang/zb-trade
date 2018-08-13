package strategy.calculate;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MaEntity implements Serializable {
	/**
	 * maEntity
	 */
	private static final long serialVersionUID = 3992550363728972752L;
	private double[] ma5;
	private double[] ma10;
	private double[] ma20;
	private double[] ma30;
	
	@Data
	@AllArgsConstructor
	public static class MaOne implements Serializable{
		private static final long serialVersionUID = 944736376415330951L;
		private double ma5;
		private double ma10;
		private double ma20;
		private double ma30;
		
		@Override
		public String toString() {
			return "均 ma5:" + ma5 + " ma10:" + ma10+ " ma20:"
					+ ma20 + " ma30:" + ma30;
		}
	}
	
	public MaOne getOne(int i){
		MaOne maOne = new MaOne(ma5[i],ma10[i],ma20[i],ma30[i]);
		return maOne;
	}
	
	public MaOne getLast(){
		int last = ma5.length - 1;
		MaOne maOne = new MaOne(ma5[last],ma10[last],ma20[last],ma30[last]);
		return maOne;
	}
}
