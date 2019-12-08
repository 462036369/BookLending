package bookLendingClient.model;

import java.io.Serializable;

public abstract class User implements Serializable{
	private static final long serialVersionUID = -2574509822709057615L;
	private String userName;
	private String password;
	private String realName;
	private String phoneNumber;
	public User(String userName, String password, String realName, String phoneNumber) {
		this.userName = userName;
		this.password = password;
		this.realName = realName;
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
