package bookLendingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import bookLendingClient.Main;
import bookLendingClient.model.Book;
import bookLendingClient.model.LendingBook;
import bookLendingClient.model.LinkedList;
import bookLendingClient.model.Student;
import bookLendingClient.model.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class StudentLendController implements Initializable{
	private int pagenum;
	@FXML TableView table;
	@FXML TableColumn isbnCol;
	@FXML TableColumn nameCol;
	@FXML TableColumn authorCol;
	@FXML TableColumn pressCol;
	@FXML TableColumn timeCol;
	@FXML TableColumn queueCol;
	@FXML TableColumn operationCol;
	@FXML Label welcomeMsg;
	@FXML Label pageMsg;
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Student user = (Student)Util.getUser();
		welcomeMsg.setText("欢迎，" + user.getRealName());
		table.setEditable(false);
		isbnCol.impl_setReorderable(false);
		nameCol.impl_setReorderable(false);
		authorCol.impl_setReorderable(false);
		pressCol.impl_setReorderable(false);
		timeCol.impl_setReorderable(false);
		queueCol.impl_setReorderable(false);
		operationCol.impl_setReorderable(false);
		isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		pressCol.setCellValueFactory(new PropertyValueFactory<>("press"));
		timeCol.setCellValueFactory(new PropertyValueFactory<>("lendTime"));
		queueCol.setCellValueFactory(new PropertyValueFactory<>("queue"));
		operationCol.setCellFactory((col) -> {
            TableCell<LendingBook, String> cell = new TableCell<LendingBook, String>() {
            	@Override
				public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        Label lendBtn = new Label("还书");
                        Pane box = new Pane();
                        box.setPrefSize(113, 18);
                        lendBtn.setLayoutX(40);
                        lendBtn.setCursor(Cursor.HAND);
                        box.getChildren().addAll(lendBtn);
                        this.setGraphic(box);
                        lendBtn.setOnMouseClicked(e -> {
                        	LendingBook lendBook = (LendingBook)table.getItems().get(this.getIndex());
                        	table.getItems().remove(lendBook);
                        	Student user = (Student)Util.getUser();
                        	user.getLendingList().remove(lendBook);
                        	Util.send("giveBack");
                        	LinkedList<Object> list = new LinkedList<>();
                        	list.add(user);
                        	list.add(lendBook);
                        	Util.send(list);
                        });
                    }
                }	
            };
            return cell;
        });
		table.setItems(Util.getLendList());
	}
	public void onClickLogout() {
		Main.changeToLogin();
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
	public void onClickPerson() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentPersonView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	public void onClickSearchView() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentSearchView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
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
}
