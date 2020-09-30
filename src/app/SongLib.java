package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent rt = FXMLLoader.load(getClass().getResource("/view/Layout.fxml"));
		primaryStage.setTitle("Song Library");
		primaryStage.setScene(new Scene(rt, 600, 400));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
