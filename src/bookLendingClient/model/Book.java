package bookLendingClient.model;

import java.io.Serializable;

public class Book implements Comparable<Book>,Serializable{
	private static final long serialVersionUID = 2747681586496663007L;
	private int id;
	private long ISBN;
	private String name;
	private String author;
	private String press;
	private double price;
	private int num;
	private Queue<User> borrowingQueue = new Queue<>();
	public Book(long ISBN, String name, String author, String press, double price,int num) {
		this.ISBN = ISBN;
		this.name = name;
		this.author = author;
		this.press = press;
		this.price = price;
		this.num = num;
	}
	public Book() {}
	
	public long getISBN() {
		return ISBN;
	}
	public void setISBN(long iSBN) {
		ISBN = iSBN;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Queue<User> getBorrowingQueue() {
		return borrowingQueue;
	}
	public void setBorrowingQueue(Queue<User> borrowingQueue) {
		this.borrowingQueue = borrowingQueue;
	}
	@Override
	public int compareTo(Book o) {
		if(this == o) {
			return 0;
		}
		if(o == null) {
			return -1;
		}
		if(this.price < o.price)
			return -1;
		else if(this.price == o.price)
			return 0;
		else
			return 1;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Book) {
			Book book = (Book)obj;
			if(book == null)
				return false;
			if(book == this) {
				return true;
			}
			if(book.ISBN == this.ISBN)
				return true;
			return false;
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return "ISBN：" + this.ISBN + "，书名：" + this.name + "，作者：" + this.author;
	}
}
