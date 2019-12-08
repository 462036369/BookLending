package bookLendingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bookLendingClient.Main;
import bookLendingClient.model.Book;
import bookLendingClient.model.Teacher;
import bookLendingClient.model.Util;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class TeacherBooksController implements Initializable{
	private int pagenum;
	@FXML TableView table;
	@FXML TableColumn isbnCol;
	@FXML TableColumn nameCol;
	@FXML TableColumn authorCol;
	@FXML TableColumn pressCol;
	@FXML TableColumn priceCol;
	@FXML TableColumn numCol;
	@FXML TableColumn operationCol;
	@FXML Label welcomeMsg;
	@FXML Label pageMsg;
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Teacher user = (Teacher)Util.getUser();
		welcomeMsg.setText("欢迎，" + user.getRealName());
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
									Util.send(Long.toString(((Book)table.getItems().get(table.getItems().size() - 1)).getISBN()));
									Util.send(book);
									Book newBook = (Book)Util.receiveObject();
									if(newBook != null)
										table.getItems().add(newBook);
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
		table.setItems(Util.getList(1));
		pagenum = 1;
	}
	public void onClickLogout() {
		Main.changeToLogin();
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
	public void onClickPerson() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/TeacherPersonView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	
	public void onClickUp() {
		if(this.pagenum == 1) {
			return;
		}
		this.pagenum--;
		pageMsg.setText("当前页：" + this.pagenum);
		table.setItems(Util.getList(pagenum));
	}
	public void onClickDown() {
		if(Util.nowList().size() < 19) {
			return;
		}
		this.pagenum++;
		pageMsg.setText("当前页：" + this.pagenum);
		table.setItems(Util.getList(pagenum));
	}
	public void onClickSearchView() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/TeacherSearchView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	public void onClickAddBook() {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/TeacherAddBookView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.showAndWait();
	}
}
