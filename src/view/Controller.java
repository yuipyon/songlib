/*
 * Actions for the GUI for in here
 */

package view;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import com.sun.glass.ui.Accessible.EventHandler;

import app.Song;
import app.sortSongName;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller extends ActionEvent {
	
	/*
	 * Here the full list of widgets used in the FXML will go we so 
	 * can reference them when modifying anything in the project.
	 */
	@FXML ListView songPlayList;
	@FXML Button edit;
	@FXML Button add;
	@FXML Button delete;
	@FXML TextField artistBox;
	@FXML TextField SongBox;
	@FXML TextField YearBox;
	@FXML TextField AlbumBox;
	
	/*
	 * ObservableList to add items to ListView which will take a 
	 * items of the Song Object that we created.
	 */
	private ObservableList<Song> songs = FXCollections.observableArrayList();
	
	
	public void addButtonAction(ActionEvent event) {
		String artist = artistBox.getText();
		String song = SongBox.getText();
		String album = AlbumBox.getText();
		String year = YearBox.getText();
		Song new_song = new Song(song, artist, album, Integer.parseInt(year));
	}
	public void editButtonAction(ActionEvent event) {
		String artist = artistBox.getText();
		String song = SongBox.getText();
		String album = AlbumBox.getText();
		String year = YearBox.getText();
	}
	public void deleteButtonAction(ActionEvent event) {
		int selectedIndex = songPlayList.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			Song songToRemove = (Song) songPlayList.getSelectionModel().getSelectedItem();
			int newSelectedIndex = 
				(selectedIndex == songPlayList.getItems().size() - 1)
					? selectedIndex - 1
					: selectedIndex;

			//removes song from songs arraylist
			songs.remove(selectedIndex);		
			//selects the next song in the list
			songPlayList.getSelectionModel().select(newSelectedIndex);
		}
	}
	
	public void start(Stage primaryStage) {
		songs.add(new Song("Hello", "Adele", "25", 2015)); 
		songs.add(new Song("Circles", "Mac Miller", "Circles", 2020)); 
		songs.add(new Song("No Role Models", "J Cole", "2014 Forest Hill Drive", 2014));
		songs.add(new Song("Acacia", "BUMP OF CHICKEN", "Pokemon GOTCHA!", 2020));
		 
		if(!songs.isEmpty()) {
			songPlayList.setItems(songs);
		}
		if(songPlayList != null) {
			Collections.sort(songs, new sortSongName());
			songPlayList.getSelectionModel().select(0);
			System.out.println(songPlayList.getItems());
		}
	}
	
	
	
		
}
