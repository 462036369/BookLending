package bookLendingClient.model;

import java.io.Serializable;

public class Teacher extends User implements Serializable{
	private static final long serialVersionUID = -2865084290682519942L;

	public Teacher(String userName, String password, String realName, String phoneNumber) {
		super(userName,password,realName,phoneNumber);
	}
}
