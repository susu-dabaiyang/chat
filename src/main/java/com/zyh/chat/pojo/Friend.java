package com.zyh.chat.pojo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 好友表实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="friend")
public class Friend implements Serializable{

	@Id
	private String id;//


	
	private String userid;//用户id
	private String friends_id;//好友id
	private String comments;//备注
	private java.util.Date createtime;//添加好友日期

	
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

	public String getFriends_id() {		
		return friends_id;
	}
	public void setFriends_id(String friends_id) {
		this.friends_id = friends_id;
	}

	public String getComments() {		
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}


	
}
