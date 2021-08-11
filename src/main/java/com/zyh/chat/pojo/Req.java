package com.zyh.chat.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 好友请求实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="friend_req")
public class Req implements Serializable{

	@Id
	private String id;//id


	
	private String from_userid;//请求好友用户id
	private String to_userid;//被请求好友用户id
	private java.util.Date createtime;//请求时间
	private String message;//发送的消息
	private Integer status;//是否已处理
	private String userpicture;//请求好友头像
	private String username;//请求好友名字

	public String getUserpicture() {
		return userpicture;
	}

	public void setUserpicture(String userpicture) {
		this.userpicture = userpicture;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getFrom_userid() {		
		return from_userid;
	}
	public void setFrom_userid(String from_userid) {
		this.from_userid = from_userid;
	}

	public String getTo_userid() {		
		return to_userid;
	}
	public void setTo_userid(String to_userid) {
		this.to_userid = to_userid;
	}

	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}

	public String getMessage() {		
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {		
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Req{" +
				"id='" + id + '\'' +
				", from_userid='" + from_userid + '\'' +
				", to_userid='" + to_userid + '\'' +
				", createtime=" + createtime +
				", message='" + message + '\'' +
				", status=" + status +
				", userpicture=" + userpicture +
				", username=" + username +
				'}';
	}
}
