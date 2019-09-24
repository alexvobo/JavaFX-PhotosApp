// Alexsandr Vobornov  Paul Keough
package app;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.LoginController;

public class photos91 extends Application {
	
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		LoginController logincontroller = loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photos Library");
		primaryStage.setResizable(false);
		primaryStage.show();
		logincontroller.start(primaryStage);

		//logincontroller.getStage(primaryStage);
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent close) {
				//SonglibController.exportLibrary();
			}
		}); 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

}
