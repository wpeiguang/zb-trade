package rest.zb.config;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodeZb {
	/**
	 * 获取错误信息(rest) @Title: getMark @Description: TODO @param @param
	 * errorCode @param @return @author: 陈文希 @return String @throws
	 */
	public static String getMark(long l) {
		return code.get(l);
	}

	private static Map<Long, String> code = new HashMap<Long, String>();
	static {
		code.put(1000l, "调用成功");
		code.put(1001l, "一般错误提示");
		code.put(1002l, "内部错误");
		code.put(1003l, "验证不通过");
		code.put(1004l, "资金安全密码锁定");
		code.put(1005l, "资金安全密码错误，请确认后重新输入");
		code.put(1006l, "实名认证等待审核或审核不通过");
		code.put(1009l, "此接口维护中");
		code.put(2001l, "人民币账户余额不足");
		code.put(2002l, "比特币账户余额不足");
		code.put(2003l, "莱特币账户余额不足");
		code.put(2005l, "以太币账户余额不足");
		code.put(2006l, "ETC币账户余额不足");
		code.put(2007l, "BTS币账户余额不足");
		code.put(2009l, "账户余额不足");
		code.put(3002l, "无效的金额");
		code.put(3003l, "无效的数量");
		code.put(3004l, "用户不存在");
		code.put(3005l, "无效的参数");
		code.put(3006l, "无效的IP或与绑定的IP不一致");
		code.put(3007l, "请求时间已失效");
		code.put(3008l, "交易记录没有找到");
		code.put(4001l, "API接口被锁定或未启用");
		code.put(4002l, "请求过于频繁");
	}
}
