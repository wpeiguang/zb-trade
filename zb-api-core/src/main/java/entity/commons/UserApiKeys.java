package entity.commons;

import java.io.File;
import java.io.IOException;

import com.jfinal.kit.PathKit;

import entity.enumtype.ExchangeEnum;
import jodd.io.FileUtil;
import jodd.props.Props;
import lombok.extern.slf4j.Slf4j;

/**
 * 可以添加多个平台,多个账户
 * @author: lianlianyi@vip.qq.com
 * @date:   2017年10月19日 下午5:57:00
 */
@Slf4j
public class UserApiKeys {
	public static UserApi userOk;
	public static UserApi userGate;
	public static UserApi userBitFinex;
	public static UserApi userZb; 
	
	static{
		Props p = new Props();
		File configFile = new File("c:/config/userKey.txt");
		if(!FileUtil.isExistingFile(configFile)){//优先读取本地的配置文件
			configFile = new File(PathKit.getRootClassPath()+"/userKey.txt");
		}
		configFile = new File("/Users/chenwenxi/etc/userKey.txt");
		if(!FileUtil.isExistingFile(configFile)){//优先读取本地的配置文件
			configFile = new File(PathKit.getRootClassPath()+"/userKey.txt");
		}
		try {
			p.load(configFile);
		} catch (IOException e) {
			log.error("读取用户apiKeys配置失败",e);
		}
		
		readOkex(p);
		readGate(p);
		readBitFinex(p);
		readZb(p);
	}
	
	private static void readBitFinex(Props p){
		userBitFinex  = new UserApi(ExchangeEnum.bitfinex,p.getValue("user.bitfinex.userName"), p.getValue("user.bitfinex.apiKey"), p.getValue("user.bitfinex.secretKey"));
	}
	
	private static void readGate(Props p){
		userGate  = new UserApi(ExchangeEnum.gate,p.getValue("user.gate.userName"), p.getValue("user.gate.apiKey"), p.getValue("user.gate.secretKey"));
	}
	
	private static void readOkex(Props p){
		userOk = new UserApi(ExchangeEnum.okex, p.getValue("user.okex.userName"), p.getValue("user.okex.apiKey"), p.getValue("user.okex.secretKey"));
		log.info("初始化okex帐号:"+userOk);
	}
	private static void readZb(Props p) {
		userZb = new UserApi(ExchangeEnum.okex, p.getValue("user.zb.userName"), p.getValue("user.zb.apikey"), p.getValue("user.zb.secretKey"));
	}
	
}
