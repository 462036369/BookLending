package bookLendingClient.model;

import java.net.Socket;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Util {
	private static final ObservableList<Book> list = FXCollections.observableArrayList();
	private static Client client;
	private static User user;
	public static ObservableList<Book> nowList(){
		return list;
	}
	public static User getUser() {
		return user;
	}
	public static void setUser(User realuser) {
		user = realuser;
	}
	public static void initClient() {
		client = Client.getInstance();
	}
	public static void initBookList(LinkedList<Book> linklist) {
		list.clear();
		Iterator<Book> it = linklist.iterator();
		while(it.hasNext()) {
			list.add(it.next());
		}
	}
	public static ObservableList<Book> getList(int pageNum) {
		client.send("getList");
		client.send(Integer.toString(pageNum));
		LinkedList<Book> linklist = (LinkedList<Book>)client.receiveObject();
		list.clear();
		Iterator<Book> it = linklist.iterator();
		while(it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
	public static void send(String msg) {
		client.send(msg);
	}
	public static void send(Object obj) {
		client.send(obj);
	}
	public static Object receiveObject() {
		return client.receiveObject();
	}
	public static String receive() {
		return client.receive();
	}
	public static void clientClose() {
		client.close();
	}
	public static ObservableList getLendList() {
		ObservableList<LendingBook> list = FXCollections.observableArrayList();
		Iterator<LendingBook> it = ((Student)user).getLendingList().iterator();
		while(it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
}
