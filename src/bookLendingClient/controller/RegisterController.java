package bookLendingClient.controller;

import java.net.URL;
import java.util.ResourceBundle;

import bookLendingClient.model.RegisterModel;
import bookLendingClient.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController implements Initializable{
	@FXML ComboBox<String> choiceType;
	@FXML TextField userName;
	@FXML PasswordField password;
	@FXML PasswordField rePassword;
	@FXML TextField realName;
	@FXML TextField phoneNumber;
	@FXML Label errorMsg;
	
	private User user;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		choiceType.getItems().addAll("教  师","学  生");
		choiceType.setPromptText("教  师");
		choiceType.setValue("教  师");
	}
	public void onClickRegister() {
		RegisterModel.onClickRegister(choiceType, userName, password, rePassword, realName, phoneNumber, errorMsg);
	}
}
