package zb.ws.entity;

import lombok.Data;

@Data
public class Cancel {
	private boolean success;
	private int code;
	private String channel;
	private String message;
	private String no;
}