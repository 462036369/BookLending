package bookLendingClient;
	
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.swing.event.TreeWillExpandListener;

import bookLendingClient.model.Book;
import bookLendingClient.model.Client;
import bookLendingClient.model.RedBlackTree;
import bookLendingClient.model.Util;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	private static Stage stage;
	
	public void test() {

	}
	@Override
	public void start(Stage primaryStage) {
		test();
		Util.initClient();
		stage = primaryStage;
		stage.setWidth(400);
		stage.setHeight(400);
		stage.setResizable(false);
		stage.setTitle("图书借阅系统");
		stage.setOnCloseRequest(e->{//点击窗口关闭时
			Util.send("quit");
			Util.clientClose();
		});
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(getClass().getResource("/bookLendingClient/view/LoginView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	public static void changeToHome(int i) {
		stage.setX(500);
		stage.setWidth(1000);
		stage.setHeight(600);
		stage.setResizable(false);
		stage.setTitle("图书借阅系统");
		Scene scene = null;
		try {
			if(i == 1)
				scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/StudentHomeView.fxml")));
			else
				scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/TeacherHomeView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(scene);
	}
	public static void changeToLogin() {
		stage.setX(800);
		stage.setWidth(400);
		stage.setHeight(400);
		stage.setResizable(false);
		stage.setTitle("图书借阅系统");
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(Main.class.getResource("/bookLendingClient/view/LoginView.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(scene);
	}
	public static void changeScene(Scene scene) {
		stage.setScene(scene);
	}
}
