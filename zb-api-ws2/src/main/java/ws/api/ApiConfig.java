package ws.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiConfig {
	private String urlWs;
	
	/**代理host*/
	private String proxyHost;
	private int proxyPoint;
	public ApiConfig(String urlWs) {
		super();
		this.urlWs = urlWs;
	}
}
