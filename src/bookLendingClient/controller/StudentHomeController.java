package bookLendingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bookLendingClient.Main;
import bookLendingClient.model.Student;
import bookLendingClient.model.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class StudentHomeController implements Initializable{
	@FXML Label welcomeMsg;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Student user = (Student)Util.getUser();
		welcomeMsg.setText("欢迎，" + user.getRealName());
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
	public void onClickLend() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentLendView.fxml")));
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
	public void onClickPerson() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentPersonView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.changeScene(scene);
	}
	
	
}
