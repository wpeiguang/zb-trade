package entity.exchange;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.hutool.core.date.DateTime;
import entity.enumtype.KTimeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KlineResult implements Serializable {

	private static final long serialVersionUID = 2911825156467469701L;

	private KTimeEnum zhouqi;
	private Date date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double vol;

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		String zq = (zhouqi != null ? "周:" + zhouqi.getTime() : "");
		return "K[时:" + zq  + ":" + new DateTime().getSeconds() + " 开:" + open + " 高:"
				+ high + " 低:" + low + ", 收:" + close + "]";
	}
}
