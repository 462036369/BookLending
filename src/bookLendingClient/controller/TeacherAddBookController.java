package bookLendingClient.controller;

import bookLendingClient.model.Book;
import bookLendingClient.model.Util;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TeacherAddBookController {
	@FXML TextField isbn;
	@FXML TextField name;
	@FXML TextField author;
	@FXML TextField press;
	@FXML TextField price;
	@FXML TextField num;
	public void onClickSure() {
		if(isbn.getText().isEmpty() ||name.getText().isEmpty() ||author.getText().isEmpty() ||press.getText().isEmpty() ||
				price.getText().isEmpty() ||num.getText().isEmpty()) {
			return;
		}
		Book book = new Book(Long.parseLong(isbn.getText()),name.getText(),author.getText(),press.getText(),Double.parseDouble(price.getText()),Integer.parseInt(num.getText()));
		Util.send("addBook");
		Util.send(book);
		((Stage)isbn.getScene().getWindow()).close();
	}
}
