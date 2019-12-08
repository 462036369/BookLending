package bookLendingClient.model;

import java.io.Serializable;

public class Student extends User implements Serializable{
	private static final long serialVersionUID = 727904744004658989L;
	private LinkedList<LendingBook> lendingList = new LinkedList<>();
	public Student(String userName, String password, String realName, String phoneNumber) {
		super(userName,password,realName,phoneNumber);
	}
	public LinkedList<LendingBook> getLendingList() {
		return lendingList;
	}
	public void setLendingList(LinkedList<LendingBook> lendingList) {
		this.lendingList = lendingList;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Student)) {
			return false;
		}
		if(obj == null)
			return false;
		Student o = (Student) obj;
		if(o == this)
			return true;
		if(o.getUserName().equals(this.getUserName())) {
			return true;
		}
		return false;
	}
	
}
