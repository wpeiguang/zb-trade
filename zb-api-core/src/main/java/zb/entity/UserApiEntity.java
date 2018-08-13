package zb.entity;

import java.io.Serializable;
import java.util.Date;

import jodd.db.oom.meta.DbColumn;
import jodd.db.oom.meta.DbId;
import jodd.db.oom.meta.DbTable;
import jodd.vtor.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import zb.enumtype.ExchangeEnum;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@DbTable(value = "UserApiEntity")
/** 用户key */
public class UserApiEntity implements Serializable{
	private static final long serialVersionUID = 2532495564964592602L;
	
	/** 主键 */
	private @NotNull @DbId(value = "id") long id;
	
	private @DbColumn(value = "exchange") String exchange;
	/**自定义用户名*/
	private @DbColumn(value = "userName") String userName;
	/**apiKey*/
	private @DbColumn(value = "apiKey") String apiKey;
	/**secretKey*/
	private @DbColumn(value = "secretKey") String secretKey;
	/**兼容chbtc,可以不写*/
	private @DbColumn(value = "payPass") String payPass;
	/** Unix 时间戳-创建 */
	private @DbColumn(value = "createTime") Date createTime;
	
	
	public UserApiEntity(ExchangeEnum zb,String userName, String apiKey, String secretKey) {
		super();
		this.userName = userName;
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.exchange = zb.name();
		this.createTime = new Date();
	}
	
	public UserApiEntity(ExchangeEnum exchange) {
		super();
		this.exchange = exchange.name();
	}
	
	


	public ExchangeEnum getExchangeEnum() {
		return ExchangeEnum.getEnum(exchange);
	}


	public void setExchangeEnum(ExchangeEnum exchange) {
		this.exchange = exchange.name();
	}





	
//	public static void main(String[] args) throws IOException, SQLException {
//		new StartModel2Db("bbcoin-entity", "src/main/java", "entity.commons").createAll();
//	}
	
}
