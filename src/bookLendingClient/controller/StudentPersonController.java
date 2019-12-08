package bookLendingClient.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bookLendingClient.Main;
import bookLendingClient.model.User;
import bookLendingClient.model.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class StudentPersonController implements Initializable{
	@FXML Label welcomeMsg;
	@FXML Label errorMsg;
	@FXML Label userName;
	@FXML PasswordField changePassword;
	@FXML TextField changeRealName;
	@FXML TextField changePhoneNum;
	private User user = Util.getUser();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		welcomeMsg.setText("欢迎，" + user.getRealName());
		userName.setText(user.getUserName());
		changePassword.setText(user.getPassword());
		changeRealName.setText(user.getRealName());
		changePhoneNum.setText(user.getPhoneNumber());
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
	public void onClickSearchView() {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentSearchView.fxml")));
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
	public void onClickSure() {
		if(changePassword.getText().equals(user.getPassword()) && changePhoneNum.getText().equals(user.getPhoneNumber()) && changeRealName.getText().equals(user.getRealName())) {
			System.out.println("No change");
			return;
		}else if(changePassword.getText().trim().length() < 6 || changePassword.getText().trim().length() > 16){
			errorMsg.setText("密码长度不得短于6位不得长于16位");
		}else if(!changePhoneNum.getText().trim().isEmpty() && !changePhoneNum.getText().trim().matches("1[3|7|4|5|8][0-9]\\d{4,8}")) {
			errorMsg.setText("请输入正确的手机号");
		}else {
			errorMsg.setText("");
			user.setPassword(changePassword.getText());
			user.setPhoneNumber(changePhoneNum.getText().trim());
			user.setRealName(changeRealName.getText().trim());
			Util.send("changeUser");
			Util.send(user);
		}
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
