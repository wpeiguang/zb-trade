// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:43:34 

//source File Name:   MA.java

package strategy.calculate;

import java.io.Serializable;
import java.util.List;

import entity.exchange.KlineResult;

public class Ma implements Serializable {
	/**
	 * MaEntity
	 */
	private static final long serialVersionUID = 8015287739111836368L;

	private List<KlineResult> m_kData = null;

	private int m_iParam[] = { 5, 10, 20, 30 };
	private double m_data[][];

	public Ma(List<KlineResult> kData) {
		m_kData = kData;
	}
	
	public Ma(List<KlineResult> kData,int[] m_iParam) {
		m_kData = kData;
		this.m_iParam = m_iParam;
	}
	

	public MaEntity getMa() {
		m_data = new double[m_iParam.length][];
		if (m_kData == null || m_kData.size() == 0) {
			return null;
		}
		MaEntity maEntity = new MaEntity();
		m_data[0] = new double[m_kData.size()];
		maEntity.setMa5(new IBase(m_kData).averageClose(m_iParam[0]));
		maEntity.setMa10(new IBase(m_kData).averageClose(m_iParam[1]));
		maEntity.setMa20(new IBase(m_kData).averageClose(m_iParam[2]));
		maEntity.setMa30(new IBase(m_kData).averageClose(m_iParam[3]));
		return maEntity;
	}
}
