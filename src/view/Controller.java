/*
 * Authors: Yulin Ni (yn140) and Karun Kanda (kk951)
 */
package view;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	        if (a.getArtist().compareToIgnoreCase(b.getArtist()) == 0) {
	        	return a.getArtist().compareToIgnoreCase(b.getArtist());
	        }
	    	return a.getArtist().compareToIgnoreCase(b.getArtist()); 
	    } 
	} 
	  
	
	/*
	 * Here the full list of widgets used in the FXML will go we so 
	 * can reference them when modifying anything in the project.
	 */
	@FXML ListView songPlayList;
	@FXML Button edit;
	@FXML Button add_button;
	@FXML Button delete_button;
	@FXML TextField ArtistBox;
	@FXML TextField SongBox;
	@FXML TextField YearBox;
	@FXML TextField AlbumBox;
	@FXML TextField SDArtistBox;
	@FXML TextField SDSongBox;
	@FXML TextField SDYearBox;
	@FXML TextField SDAlbumBox;
	
	
	/*
	 * 1. ObservableList to add items to ListView which will take a 
	 *    items of the Song Object that we created.
	 * 2. ArrayList is meant so we can implement a alphabetical sorting algorithm on the 
	 *    input list given.
	 */
	private ObservableList<Song> songs = FXCollections.observableArrayList();
	private ArrayList<Song> songList = new ArrayList<Song>();
	Song temp; //placeholder value
	
	public void addButtonAction(ActionEvent event) {
		String artist = ArtistBox.getText();
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
		
		ArtistBox.setText("");
		SongBox.setText("");
		AlbumBox.setText("");
		YearBox.setText("");
	}
	
	private boolean checkElements(ArrayList<Song> songs, Song item) {
		for(int i = 0; i<songs.size(); i++) {
			if(item.equals(songs.get(i))) {
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
	
	private boolean findDuplicate(ArrayList<Song> songs, Song item) {
		boolean position = false;
		for(int i = 0; i<songs.size(); i++) {
			if((item.getArtist().compareToIgnoreCase(songs.get(i).getArtist()) == 0) && (item.getName().compareToIgnoreCase(songs.get(i).getName()) == 0)){
				position = true;
				return position;
			} 
		}
		return position;
	}
	
	private void replaceDetails(ArrayList<Song> songs1, Song item) {
		for(int i = 0; i<songs1.size(); i++) {
			if((item.getArtist().compareToIgnoreCase(songs.get(i).getArtist()) == 0) || (item.getName().compareToIgnoreCase(songs.get(i).getName()) == 0)){
				songList.remove(songs1.get(i));
				boolean dup = findDuplicate(songList, item);
				if(dup == true) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Duplicate Entry");
					alert.setHeaderText("This song already exists in your library");
					alert.setContentText("Please input another song");
					alert.showAndWait();
					break;
				} else {
					songList.add(item);
					Collections.sort(songList, new sortSongName());
					songs = FXCollections.observableList(songList);
					songPlayList.setItems(songs);
					break;
				}
			} 
			break;
		}
	}
	
	public void editButtonAction(ActionEvent event) {
		String artist = SDArtistBox.getText();
		String song = SDSongBox.getText();
		String album = SDAlbumBox.getText();
		String year = SDYearBox.getText();
		Song newSong = new Song(song, artist, album, year);
		replaceDetails(songList, newSong);
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
			SDArtistBox.setText(song.getArtist());
			SDSongBox.setText(song.getName());
			SDAlbumBox.setText(song.getAlbum());
			SDYearBox.setText(song.getYear());  
		}
	}
	
	public void start(Stage primaryStage) {
		try {
			File file = new File("user_data/user_data.txt");
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String name = "";
				String artist = "";
				String album = "";
				String year = "";
				Song loadSong = new Song(name, artist, album, year);
				loadSong.setName(scan.nextLine());
				loadSong.setArtist(scan.nextLine());
				loadSong.setAlbum(scan.nextLine());
				loadSong.setYear(scan.nextLine());
				songList.add(loadSong);
			}
		} catch (FileNotFoundException e) {
			System.out.println("No existing library found, starting fresh!");
		}
		songs = FXCollections.observableList(songList);		
		songPlayList.setItems(songs);
		songPlayList.getSelectionModel().select(0);
				
		/*
		Text text1 = new Text("Something");
		Text text2 = new Text("Something Else");
		SongDetails.getChildren().add(text1);
		*/
		
		//random comment for checking git thing - delete if u see this
		primaryStage.setOnCloseRequest(event -> {
		    try {
				FileWriter wr = new FileWriter("user_data/user_data.txt");
				for (Song song: songList) {
					wr.write(song.getName() + "\n");
					wr.write(song.getArtist() + "\n");
					wr.write(song.getAlbum() + "\n");
					wr.write(song.getYear() + "\n");
				}
			    System.out.println("Saved your library");
				wr.flush();
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}			
}