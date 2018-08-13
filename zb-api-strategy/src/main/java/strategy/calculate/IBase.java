package strategy.calculate;

import java.util.List;

import entity.exchange.KlineResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IBase {
	private List<KlineResult> m_kData;

	public double[] averageClose(int iParam) {
		double data[] = new double[m_kData.size()];
		if (m_kData == null || m_kData.size() == 0)
			return null;
		int n = iParam;
		if (n > m_kData.size() || n < 1)
			return null;
		double preClose = 0.0F;
		double sum = 0.0D;
		for (int i = 0; i < n - 1; i++)
			sum += m_kData.get(i).getClose();

		for (int i = n - 1; i < m_kData.size(); i++) {
			sum -= preClose;
			sum += m_kData.get(i).getClose();
			data[i] = (double) (sum / (double) n);
			preClose = m_kData.get((i - n) + 1).getClose();
		}
		return data;
	}
}
