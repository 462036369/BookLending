package bookLendingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import bookLendingClient.Main;
import bookLendingClient.model.Book;
import bookLendingClient.model.LinkedList;
import bookLendingClient.model.Teacher;
import bookLendingClient.model.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TeacherSearchControllor implements Initializable{
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
		Teacher user = (Teacher)Util.getUser();
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
                    if (!empty && !isbnCol.getText().isEmpty()) {
                        Label btn = new Label("修改/删除");
                        //lendBtn.setFont(new Font(5));
                        Pane box = new Pane();
                        box.setPrefSize(113, 18);
                        btn.setLayoutX(30);
                        btn.setCursor(Cursor.HAND);
                        box.getChildren().addAll(btn);
                        this.setGraphic(box);
                        btn.setOnMouseClicked(e -> {
                        	Stage stage = new Stage();
                        	stage.initModality(Modality.APPLICATION_MODAL);
                        	HBox hbox = new HBox();
                        	Book book = (Book)table.getItems().get(this.getIndex());
                        	VBox nameBox = new VBox();
                        	Label nameLabel = new Label("书名");
                        	nameLabel.setAlignment(Pos.CENTER);
                        	nameLabel.setPrefWidth(113);
                        	TextField nameText = new TextField(book.getName());
                        	nameText.setPrefWidth(113);
                        	nameBox.getChildren().addAll(nameLabel,nameText);
                        	
                        	VBox pressBox = new VBox();
                        	Label pressLabel = new Label("书名");
                        	pressLabel.setAlignment(Pos.CENTER);
                        	pressLabel.setPrefWidth(113);
                        	TextField pressText = new TextField(book.getPress());
                        	pressText.setPrefWidth(113);
                        	pressBox.getChildren().addAll(pressLabel,pressText);
                        	
                        	VBox priceBox = new VBox();
                        	Label priceLabel = new Label("定价");
                        	priceLabel.setAlignment(Pos.CENTER);
                        	priceLabel.setPrefWidth(113);
                        	TextField priceText = new TextField(Double.toString(book.getPrice()));
                        	priceText.setPrefWidth(113);
                        	Button delete = new Button("删除此书");
                        	delete.setOnAction(new EventHandler<ActionEvent>() {
                        		
								@Override
								public void handle(ActionEvent event) {
									table.getItems().remove(book);
									Util.send("removeBook");
									Util.send("123");
									Util.send(book);
									Book newBook = (Book)Util.receiveObject();
									
									table.refresh();
									stage.close();
								}
                        		
							});
                        	priceBox.getChildren().addAll(priceLabel,priceText,delete);
                        	
                        	VBox numBox = new VBox();
                        	Label numLabel = new Label("库存");
                        	numLabel.setAlignment(Pos.CENTER);
                        	numLabel.setPrefWidth(113);
                        	TextField numText = new TextField(Integer.toString(book.getNum()));
                        	numText.setPrefWidth(113);
                        	numBox.getChildren().addAll(numLabel,numText);
                        	
                        	
                        	VBox authorBox = new VBox();
                        	Label authorLabel = new Label("作者");
                        	authorLabel.setAlignment(Pos.CENTER);
                        	authorLabel.setPrefWidth(113);
                        	TextField authorText = new TextField(book.getAuthor());
                        	authorText.setPrefWidth(113);
                        	Button sure = new Button("确定修改");
                        	sure.setOnAction(new EventHandler<ActionEvent>() {
								
								@Override
								public void handle(ActionEvent arg0) {
									book.setName(nameText.getText());
									book.setAuthor(authorText.getText());
									book.setPress(pressText.getText());
									book.setPrice(Double.parseDouble(priceText.getText()));
									book.setNum(Integer.parseInt(numText.getText()));
									Util.send("changeBook");
									Util.send(book);
									table.refresh();
									stage.close();
								}
							});
                        	authorBox.getChildren().addAll(authorLabel,authorText,sure);
                        	
                        	
                        	
                        	
                        	
                        	
                        	hbox.getChildren().addAll(nameBox,authorBox,pressBox,priceBox,numBox);
                        	stage.setScene(new Scene(hbox));
                        	stage.showAndWait();
                        	
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
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/TeacherBooksView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	public void onClickHome() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/TeacherHomeView.fxml")));
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
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/TeacherPersonView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	
}
