package bookLendingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.ResourceBundle;

import bookLendingClient.Main;
import bookLendingClient.model.Book;
import bookLendingClient.model.LendingBook;
import bookLendingClient.model.LinkedList;
import bookLendingClient.model.Student;
import bookLendingClient.model.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class StudentSearchControllor implements Initializable{
	@FXML ComboBox<String> choiceType;
	@FXML Label welcomeMsg;
	@FXML TableView table;
	@FXML TableColumn isbnCol;
	@FXML TableColumn nameCol;
	@FXML TableColumn authorCol;
	@FXML TableColumn pressCol;
	@FXML TableColumn priceCol;
	@FXML TableColumn numCol;
	@FXML TableColumn operationCol;
	@FXML Button search;
	@FXML TextField searchKey;
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Student user = (Student)Util.getUser();
		welcomeMsg.setText("欢迎，" + user.getRealName());
		choiceType.getItems().addAll("按 ISBN","按 书名","按 作者");
		choiceType.setPromptText("按 ISBN");
		choiceType.setValue("按 ISBN");
		table.setEditable(false);
		isbnCol.impl_setReorderable(false);
		nameCol.impl_setReorderable(false);
		authorCol.impl_setReorderable(false);
		pressCol.impl_setReorderable(false);
		priceCol.impl_setReorderable(false);
		numCol.impl_setReorderable(false);
		operationCol.impl_setReorderable(false);
		isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		pressCol.setCellValueFactory(new PropertyValueFactory<>("press"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		numCol.setCellValueFactory(new PropertyValueFactory<>("num"));
		operationCol.setCellFactory((col) -> {
            TableCell<Book, String> cell = new TableCell<Book, String>() {
            	@Override
				public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        Label lendBtn = new Label("借阅");
                        //lendBtn.setFont(new Font(5));
                        Pane box = new Pane();
                        box.setPrefSize(113, 18);
                        lendBtn.setLayoutX(40);
                        lendBtn.setCursor(Cursor.HAND);
                        box.getChildren().addAll(lendBtn);
                        this.setGraphic(box);
                        lendBtn.setOnMouseClicked(e -> {
                        	Book lendBook = (Book)table.getItems().get(this.getIndex());
                        	Student user = (Student)Util.getUser();
                        	if(lendBook.getNum() == 0) {
                        		lendBook.getBorrowingQueue().push(user);
                        	}else {
                        		lendBook.setNum(lendBook.getNum() - 1);
                        	}
                        	table.refresh();
                        	LendingBook lendingBook = new LendingBook(lendBook,LocalDate.now());
                        	user.getLendingList().add(lendingBook);
                        	Util.send("lending");
                        	LinkedList<Object> list = new LinkedList<>();
                        	list.add(user);
                        	list.add(lendBook);
                        	list.add(lendingBook);
                        	Util.send(list);
                        });
                    }
                }	
            };
            return cell;
        });
	}
	public void onClickLogout() {
		Main.changeToLogin();
	}
	public void onClickBooks() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentBooksView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	public void onClickHome() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentHomeView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	public void onClickSearch() {
		
		String key = searchKey.getText().trim();
		if(key.isEmpty()) {
			table.setItems(null);
			return;
		}
		Util.send("search");
		if(choiceType.getValue().equals("按 ISBN")) {
			Util.send("ISBN");
			try {
				Long.parseLong(key);
			}catch(NumberFormatException e) {
				Util.send("-1");
				table.setItems(null);
				return;
			}
		}else if(choiceType.getValue().equals("按 书名")) {
			Util.send("bookName");
		}else {
			Util.send("authorName");
		}
		Util.send(key);
		LinkedList<Book> respond = (LinkedList<Book>) Util.receiveObject();
		if(respond == null) {
			table.setItems(null);
			return;
		}
		ObservableList list = FXCollections.observableArrayList();
		Iterator<Book> it = respond.iterator();
		while(it.hasNext()) {
			Book temp = it.next();
			if(temp != null)
				list.add(temp);
		}
		table.setItems(null);
		table.setItems(list);
	}
	public void onClickPerson() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentPersonView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	public void onClickLend() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentLendView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
}
