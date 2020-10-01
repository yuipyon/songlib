//Yulin Ni (yn140) and Karun Kanda (kk951)
package app;

import view.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.Controller;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Layout.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		Controller controller = loader.getController();
		controller.start(primaryStage);
		
		primaryStage.setTitle("Song Library");
		primaryStage.setScene(new Scene(root, 621, 424));
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}

}
