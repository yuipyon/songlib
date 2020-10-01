//Yulin Ni (yn140) and Karun Kanda (kk951)
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
	        if (a.getName().compareToIgnoreCase(b.getName()) == 0) {
	        	return a.getArtist().compareToIgnoreCase(b.getArtist());
	        }
	    	return a.getName().compareToIgnoreCase(b.getName()); 
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
	@FXML TextFlow SongDetails;
	
	/*
	 * 1. ObservableList to add items to ListView which will take a 
	 *    items of the Song Object that we created.
	 * 2. ArrayList is meant so we can implement a alphabetical sorting algorithm on the 
	 *    input list given.
	 */
	private ObservableList<Song> songs = FXCollections.observableArrayList();
	private ArrayList<Song> songList = new ArrayList<Song>();
	
	private void addAction() {
		String artist = artistBox.getText();
		String song = SongBox.getText();
		String album = AlbumBox.getText();
		String year = YearBox.getText();
		
		Song new_song = new Song(song, artist, album, year);
		boolean exists = checkElements(songList, new_song);
		
		if(artist.equals("") || song.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Invalid Input");
			alert.setHeaderText("No input for song and/or artist");
			alert.setContentText("Please provide at least a song and artist name.");
			alert.showAndWait();
		}
		else if(exists == true) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Duplicate Entry");
			alert.setHeaderText("This song already exists in your library");
			alert.setContentText("Please input another song");
			alert.showAndWait();
		}
		else {
			songList.add(new_song);
			Collections.sort(songList, new sortSongName());
			int position = findIndex(songList, new_song);
			songs = FXCollections.observableList(songList);
			songPlayList.setItems(songs);
			songPlayList.getSelectionModel().select(position);
		}
		
		artistBox.setText("");
		SongBox.setText("");
		AlbumBox.setText("");
		YearBox.setText("");
	}
	
	public void addButtonAction(ActionEvent event) {
		addAction();
	}
	
	private boolean checkElements(ArrayList<Song> songs, Song item) {
		for(int i = 0; i<songs.size(); i++) {
			if((item.getArtist().compareToIgnoreCase(songs.get(i).getArtist()) == 0) && (item.getName().compareToIgnoreCase(songs.get(i).getName()) == 0)) {
				return true;
			} 
		}
		return false;
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
		
		//Outside of the if statement we can start changing the elements of the thing
		/*
		 * First Step: Select the song
		 * Second Step: Edit the parts you want to edit
		 * Third Step: Remove the previous part from the array list
		 * Fourth Step: Add the arraylist element to ListView
		 */
		System.out.println("Clicked");
		int selectedIndex = songPlayList.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			Song song = (Song) songPlayList.getSelectionModel().getSelectedItem();
			artistBox.setText(song.getArtist());
			SongBox.setText(song.getName());
			AlbumBox.setText(song.getAlbum());
			YearBox.setText(song.getYear());
		}
		
			
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
	
	public void selectButtonAction(ActionEvent event) {
		int selectedIndex = songPlayList.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			Song song = (Song) songPlayList.getSelectionModel().getSelectedItem();
			Text details = new Text("Details for" + " " + song.getName() + ":" + "\n");
			SongDetails.getChildren().add(details);
			Text songName = new Text("Song Name: " + song.getName() + "\n");
			SongDetails.getChildren().add(songName);
			Text artist = new Text("Artist Name: " + song.getArtist() + "\n");
			SongDetails.getChildren().add(artist);
			Text album = new Text("Album Name: " + song.getAlbum() + "\n");
			SongDetails.getChildren().add(album);
			Text year = new Text("Year: " + song.getYear() + "\n");
			SongDetails.getChildren().add(year);
		}
	}
	
	public void start(Stage primaryStage) {
		//here, we should read saved song list text file, load it into songList, and display it in songPlayList
		//don't need to alphabetically sort here because text file should ideally already be sorted (went through sorting process in add/edit method)
		songs = FXCollections.observableList(songList);		
		songPlayList.setItems(songs);
		songPlayList.getSelectionModel().select(0);
		
		/*
		Text text1 = new Text("Something");
		Text text2 = new Text("Something Else");
		SongDetails.getChildren().add(text1);
		*/
		
		primaryStage.setOnCloseRequest(event -> {

		    System.out.println("Saved your library");

		    //System.out.println("Stage is closing");

		    try {
				FileWriter wr = new FileWriter("user_data/user_data.txt");
				for(Song song: songList) {
					wr.write(song.toString() + "\n");
				}
				wr.flush();
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		});
	}			
}
