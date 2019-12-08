package bookLendingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bookLendingClient.Main;
import bookLendingClient.model.Teacher;
import bookLendingClient.model.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class TeacherHomeController implements Initializable{
	@FXML Label welcomeMsg;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Teacher user = (Teacher)Util.getUser();
		welcomeMsg.setText("欢迎，" + user.getRealName());
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
	
	public void onClickSearchView() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/TeacherSearchView.fxml")));
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
	
	
}
