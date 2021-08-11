package com.zyh.chat.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="users")
public class Users implements Serializable{

	@Id
	private String userid;//用户id


	
	private String username;//用户名
	private String password;//密码
	private String phonenumber;//手机号
	private String name;//姓名/昵称
	private String major;//专业
	private String experience;//个人经历
	private Integer age;//年龄
	private String sex;//性别
	private String current_state;//当前状态
	private Integer qqnum;//qq号码
	private String undergraduateschool;//毕业学校
	private String picture;//照片
	private String email;//邮箱
	private java.util.Date birthday;//出生年月/生日
	private String major_fk;//专业id
	private String direction;//大四选择的方向
	private String isshow;//大四选择的方向

	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public Integer getQqnum() {
		return qqnum;
	}

	public void setQqnum(Integer qqnum) {
		this.qqnum = qqnum;
	}

	public String getUndergraduateschool() {
		return undergraduateschool;
	}

	public void setUndergraduateschool(String undergraduateschool) {
		this.undergraduateschool = undergraduateschool;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {		
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {		
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getMajor() {		
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}

	public String getExperience() {		
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}

	public Integer getAge() {		
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {		
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCurrent_state() {		
		return current_state;
	}
	public void setCurrent_state(String current_state) {
		this.current_state = current_state;
	}



	public String getPicture() {		
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getEmail() {		
		return email;
	}
	public void setEmail(String Email) {
		this.email = Email;
	}

	public java.util.Date getBirthday() {		
		return birthday;
	}
	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}

	public String getMajor_fk() {
		return major_fk;
	}
	public void setMajor_fk(String major_fk) {
		this.major_fk = major_fk;
	}

	public String getDirection() {		
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "Users{" +
				"userid='" + userid + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", phonenumber='" + phonenumber + '\'' +
				", name='" + name + '\'' +
				", major='" + major + '\'' +
				", experience='" + experience + '\'' +
				", age=" + age +
				", sex='" + sex + '\'' +
				", current_state='" + current_state + '\'' +
				", qqnum=" + qqnum +
				", undergraduateschool='" + undergraduateschool + '\'' +
				", picture='" + picture + '\'' +
				", email='" + email + '\'' +
				", birthday=" + birthday +
				", major_fk='" + major_fk + '\'' +
				", direction='" + direction + '\'' +
				'}';
	}
}
