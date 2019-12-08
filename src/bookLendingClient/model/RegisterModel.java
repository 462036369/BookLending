package bookLendingClient.model;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterModel {
	private static Client client = Client.getInstance();
	public static void onClickRegister(ComboBox<String> choiceType,TextField userName,PasswordField password,
			PasswordField rePassword,TextField realName,TextField phoneNumber,Label errorMsg) {
		if(realName.getText().trim().isEmpty() || userName.getText().trim().isEmpty() || phoneNumber.getText().trim().isEmpty() || password.getText().isEmpty()) {
			errorMsg.setText("请填写全部信息");
		}else if(userName.getText().trim().length() < 5 || userName.getText().trim().length() > 20){
			errorMsg.setText("用户名长度不得短于5位不得长于20位");
		}else if(password.getText().trim().length() < 6 || password.getText().trim().length() > 16){
			errorMsg.setText("密码长度不得短于6位不得长于16位");
		}else if(!password.getText().equals(rePassword.getText())) {
			errorMsg.setText("两次密码不一致");
		}else if(!phoneNumber.getText().trim().isEmpty() && !phoneNumber.getText().trim().matches("1[3|7|4|5|8][0-9]\\d{4,8}")) {
			errorMsg.setText("请输入正确的手机号");
		}else if(!verificationUserName(userName.getText().trim())) {
			errorMsg.setText("用户名已存在");
		}else {
			User user;
			client.send("register");
			if(choiceType.getValue().equals("教  师")) {
				user = new Teacher(userName.getText().trim(), password.getText(), realName.getText(), phoneNumber.getText());
				client.send(user);
			}else {
				user = new Student(userName.getText().trim(), password.getText(), realName.getText(), phoneNumber.getText());
				client.send(user);
			}
			errorMsg.setText("");
			Stage stage = (Stage)errorMsg.getScene().getWindow();
			stage.close();
		}
	}
	private static boolean verificationUserName(String userName) {
		client.send("verificationUserName");
		client.send(userName);
		String respond = client.receive();
		if(respond.equals("exist")) {
			return false;
		}else {
			return true;
		}
	}
}
