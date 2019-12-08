package bookLendingClient.controller;

import java.io.IOException;

import bookLendingClient.Main;
import bookLendingClient.model.Student;
import bookLendingClient.model.User;
import bookLendingClient.model.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController{
	@FXML TextField userName;
	@FXML PasswordField password;
	@FXML Label errorMsg;
	public void onClickRegister() {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		stage.setWidth(250);
		stage.setHeight(400);
		stage.setTitle("注册");
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(getClass().getResource("/bookLendingClient/view/RegisterView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.showAndWait();
	}
	public void onClickLogin() {
		if(userName.getText().isEmpty() || password.getText().isEmpty()) {
			errorMsg.setText("用户名或密码不能为空");
			return;
		}
		Util.send("login");
		Util.send(userName.getText().trim());
		Util.send(password.getText());
		String respond = Util.receive();
		if(respond.equals("noUser")) {
			errorMsg.setText("用户名不存在");
		}else if(respond.equals("passwordError")) {
			errorMsg.setText("密码错");
		}else {
			errorMsg.setText("");
			User user = (User)Util.receiveObject();
			Util.setUser(user);
			if(user instanceof Student)
				Main.changeToHome(1);
			else
				Main.changeToHome(2);
		}
	}

	
}
