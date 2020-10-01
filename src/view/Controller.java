/*
 * Actions for the GUI for in here
 */

package view;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.FileWriter;
import java.io.IOException;

import com.sun.glass.ui.Accessible.EventHandler;

import app.Song;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	 * The Comparator implementation was meant so we can compare the objects while using
	 * the Collections.sort() method
	 */
	class sortSongName implements Comparator<Song> 
	{  
	    public int compare(Song a, Song b) 
	    { 
	        return a.getName().compareTo(b.getName()); 
	    } 
	} 
	  
	
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
	 * 1. ObservableList to add items to ListView which will take a 
	 *    items of the Song Object that we created.
	 * 2. ArrayList is meant so we can implement a alphabetical sorting algorithm on the 
	 *    input list given.
	 */
	private ObservableList<Song> songs = FXCollections.observableArrayList();
	private ArrayList<Song> songList = new ArrayList<Song>();
	
	public void addButtonAction(ActionEvent event) {
		String artist = artistBox.getText();
		String song = SongBox.getText();
		String album = AlbumBox.getText();
		String year = YearBox.getText();
		if(artist.equals("") || song.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Invalid Input");
			alert.setHeaderText("No input for song and/or artist");
			alert.setContentText("Please provide atleast a song and artist name.");
			alert.showAndWait();
			add.setDisable(true);
		}
		if(year.equals("")) {
			year = "0";
		}
		Song new_song = new Song(song, artist, album, Integer.parseInt(year));
		boolean exists = checkElements(songList, new_song);
		if(exists == true) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Duplicate Entry");
			alert.setHeaderText("This input you gave is the same as a previous one");
			alert.setContentText("Please input another song");
			alert.showAndWait();
			add.setDisable(true);
		}
		songList.add(new_song);
		Collections.sort(songList, new sortSongName());
		int position = findIndex(songList, new_song);
		songs = FXCollections.observableList(songList);
		songPlayList.setItems(songs);
		songPlayList.getSelectionModel().select(position);
	}
	
	private boolean checkElements(ArrayList<Song> songs, Song item) {
		boolean exists = false;
		for(int i = 0; i<songs.size(); i++) {
			if((item.getArtist().contains(songs.get(i).getArtist())) && (item.getName().contains(songs.get(i).getName()))) {
				exists = true;
				break;
			} 
		}
		return exists;
	}
	
	private int findIndex(ArrayList<Song> songs, Song item) {
		int position = 0;
		for(int i = 0; i<songs.size(); i++) {
			if((item.getName().equals(songs.get(i).getName()))){
				position = i;
				break;
			} 
		}
		return position;
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
		
		Collections.sort(songList, new sortSongName());
		songs = FXCollections.observableList(songList);		
		songPlayList.setItems(songs);
		songPlayList.getSelectionModel().select(0);
		
		primaryStage.setOnCloseRequest(event -> {
		    System.out.println("Stage is closing");
		    try {
				FileWriter wr = new FileWriter("user_data/user_data.txt");
				for(Song song: songList) {
					wr.write(song.toString());
				}
				wr.flush();
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		});
	}			
}
