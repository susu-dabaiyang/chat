package com.zyh.chat.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 聊天记录实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="chat_record")
public class Record implements Serializable{

	@Id
	private String id;//id


	
	private String userid;//用户id
	private String friendid;//好友id
	private Integer has_read;//是否已读
	private java.util.Date createtime;//聊天时间
	private Integer has_delete;//是否删除
	private String message;//消息
	private String userpic;//用户头像
	private String friendpic;//好友头像

	public String getUserpic() {
		return userpic;
	}

	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}

	public String getFriendpic() {
		return friendpic;
	}

	public void setFriendpic(String friendpic) {
		this.friendpic = friendpic;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {		
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getFriendid() {		
		return friendid;
	}
	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	public Integer getHas_read() {		
		return has_read;
	}
	public void setHas_read(Integer has_read) {
		this.has_read = has_read;
	}

	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}

	public Integer getHas_delete() {		
		return has_delete;
	}
	public void setHas_delete(Integer has_delete) {
		this.has_delete = has_delete;
	}

	public String getMessage() {		
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Record{" +
				"id='" + id + '\'' +
				", userid='" + userid + '\'' +
				", friendid='" + friendid + '\'' +
				", has_read=" + has_read +
				", createtime=" + createtime +
				", has_delete=" + has_delete +
				", message='" + message + '\'' +
				'}';
	}
}
