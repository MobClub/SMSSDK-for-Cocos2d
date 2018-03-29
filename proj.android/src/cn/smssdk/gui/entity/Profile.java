package cn.smssdk.gui.entity;

import java.io.Serializable;

/**
 * Created by weishj on 2018/3/14.
 */

public class Profile implements Serializable {
	private String nickName;
	private String phoneNum;
	private String avatar;
	private String country;
	private String uid;

	public Profile(String nickName, String phoneNum, String avatar, String country, String uid) {
		this.nickName = nickName;
		this.phoneNum = phoneNum;
		this.avatar = avatar;
		this.country = country;
		this.uid = uid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
