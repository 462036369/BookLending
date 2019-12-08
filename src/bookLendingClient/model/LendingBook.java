package bookLendingClient.model;

import java.io.Serializable;
import java.time.LocalDate;

public class LendingBook implements Serializable{
	private static final long serialVersionUID = 3175214325730616888L;
	private String lendTime;
	private Book book;
	public LendingBook(Book book,LocalDate lendTime) {
			this.book = book;
			this.lendTime = lendTime.toString();
	}
	public String getLendTime() {
		return lendTime.toString();
	}
	public void setLendTime(String lendTime) {
		this.lendTime = lendTime;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	public long getISBN() {
		return this.book.getISBN();
	}
	public void setISBN(long iSBN) {
		this.book.setISBN(iSBN);
	}
	public String getName() {
		return this.book.getName();
	}
	public void setName(String name) {
		this.book.setName(name);
	}
	public String getAuthor() {
		return this.book.getAuthor();
	}
	public void setAuthor(String author) {
		this.book.setAuthor(author);
	}
	public String getPress() {
		return this.book.getPress();
	}
	public void setPress(String press) {
		this.book.setPress(press);
	}
	public int getQueue() {
		Util.send("getBook");
		Util.send(Long.toString(this.book.getISBN()));
		this.book = (Book)Util.receiveObject();
		return this.book.getBorrowingQueue().getPos(Util.getUser());
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof LendingBook))
			return false;
		LendingBook o = (LendingBook)obj;
		if(o == this) {
			return true;
		}
		if(o == null)
			return false;
		if(this.lendTime.equals(o.lendTime) && this.book.equals(o.book)) {
			return true;
		}
		return false;
	}
}
