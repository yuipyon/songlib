/*
 * Actions for the GUI for in here
 */

package view;

import javafx.event.ActionEvent;

import com.sun.glass.ui.Accessible.EventHandler;

import app.Song;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller extends ActionEvent {
	@FXML ListView<Song> songPlayList;
	@FXML Button edit;
	@FXML Button add;
	@FXML Button delete;
	@FXML TextField artistBox;
	@FXML TextField SongBox;
	@FXML TextField YearBox;
	@FXML TextField AlbumBox;
	
	private ObservableList<Song> songs = FXCollections.observableArrayList();
	
	@FXML
	private void addButtonAction(ActionEvent event) {
		String artist = artistBox.getText();
		String song = SongBox.getText();
		String album = AlbumBox.getText();
		String year = YearBox.getText();
		Song new_song = new Song(song, artist, album, Integer.parseInt(year));
		songs.add(new_song);
		songPlayList = new ListView<Song>(songs);
		System.out.println(songPlayList);
	}
}
