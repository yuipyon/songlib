/*
 * @author: Yulin Ni (yn140) 
 * @author: Karun Kanda (kk951)
 * @version: 1.0
 */

package view;

import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import app.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/*
 * ------------------------------------------------------------------------------------------------
 * 
 * Controller Class
 * 
 * Description: Object that holds all the methods that the GUI utilizes to make the GUI interactive.
 * 
 * -------------------------------------------------------------------------------------------------
 */

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
	ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
	
	/*
	 * ---------------------------
	 * addButtonAction Method
	 * 
	 * Adds a song to the playlist
	 * ----------------------------
	 */
	public void addButtonAction(ActionEvent event) {
		String artist = ArtistBox.getText();
		String song = SongBox.getText();
		String album = AlbumBox.getText();
		String year = YearBox.getText();
		
		Song new_song = new Song(song, artist, album, year);
		boolean exists = checkElements(songList, new_song);
		
		if(artist.isBlank() || song.isBlank()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Invalid Input");
			alert.setHeaderText("No input for song and/or artist");
			alert.setContentText("Please provide at least a song and artist name.");
			alert.showAndWait();
		} 
		else if(!year.matches("^[0-9]{4}$")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Duplicate Entry");
			alert.setHeaderText("That is not a year");
			alert.setContentText("Please input the correct format for year");
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
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to add " + new_song.getName() + " by " + new_song.getArtist() + " to your library?", yes, ButtonType.CANCEL);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == yes) {
				songList.add(new_song);
				Collections.sort(songList, new sortSongName());
				int position = findIndex(songList, new_song);
				songs = FXCollections.observableList(songList);
				songPlayList.setItems(songs);
				songPlayList.getSelectionModel().select(position);
				ArtistBox.setText("");
				SongBox.setText("");
				AlbumBox.setText("");
				YearBox.setText("");
				SDArtistBox.setText(new_song.getArtist());
		        SDSongBox.setText(new_song.getName());
		        SDAlbumBox.setText(new_song.getAlbum());
		        SDYearBox.setText(new_song.getYear());
			}
		}
	}
	
	/*
	 * Below are some helper methods utilized in the addButtonAction
	 */
	
	public static boolean isBlank(String s)
	{
	    return (s == null) || (s.trim().length() == 0);
	}
	
	/* The checkElements Method purpose is to find a duplicate song and return true if found and return false if there isn't a duplicate song. */
	private boolean checkElements(ArrayList<Song> songs, Song item) {
		for(int i = 0; i<songs.size(); i++) {
			if((item.getArtist().compareToIgnoreCase(songs.get(i).getArtist()) == 0) && (item.getName().compareToIgnoreCase(songs.get(i).getName()) == 0)) {
				return true;
			} 
		}
		return false;
	}
	
	/* The findIndex Method returns the index position where the item and a song in the arraylist match according to there name. */
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
	
	public static boolean isBlank(String s)
	{
	    return (s == null) || (s.trim().length() == 0);
	}
	
	/*
	 * --------------------------------------------------
	 * editButtonAction Method
	 * 
	 * Edits the details of selected song in the playlist
	 * --------------------------------------------------
	 */
	public void editButtonAction(ActionEvent event) {
        Song beingEdited = (Song) songPlayList.getSelectionModel().getSelectedItem();
        String artist = SDArtistBox.getText();
        String song = SDSongBox.getText();
        String album = SDAlbumBox.getText();
        String year = SDYearBox.getText();
        if(artist.isBlank() || song.isBlank()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Invalid Input");
			alert.setHeaderText("No input for song and/or artist");
			alert.setContentText("Please provide at least a song and artist name.");
			alert.showAndWait();
		}
        if(!year.matches("^[0-9]{4}$")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Duplicate Entry");
			alert.setHeaderText("That is not a year");
			alert.setContentText("Please input the correct format for year");
			alert.showAndWait();
		}
        Song newSong = new Song(song, artist, album, year);
		if(newSong.getName().isBlank() || newSong.getArtist().isBlank()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid action");
			alert.setContentText("Please include either the song title or artist name");
			alert.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit " + beingEdited.getName() + " by " + beingEdited.getArtist() + "?", yes, ButtonType.CANCEL);
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.get() == yes) {
		        replaceDetails(songList, newSong);
	        }
		}
    }

	
	/*
	 * Below are some helper methods utilized in the editButtonAction
	 */
		
     /* The replaceDetails method is to find the song that you need to replace, delete that individual song and replace it with the new detail that the user chooses to insert.*/
		private void replaceDetails(ArrayList<Song> songs1, Song item) {
			boolean dup = findDuplicate(songList, item);
			if(dup == true) {
				boolean dupe = findDuplicatePt2(songList, item);
				if(dupe == true) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Duplicate Entry");
					alert.setHeaderText("This song already exists in your library");
					alert.setContentText("Please input another song");
					alert.showAndWait();
				} else {
					songList.remove(songPlayList.getSelectionModel().getSelectedIndex());
					songList.add(item);
					Collections.sort(songList, new sortSongName());
					int position = findIndex(songList, item);
					songs = FXCollections.observableList(songList);
					songPlayList.setItems(songs);
					songPlayList.getSelectionModel().select(position);
			        SDArtistBox.setText("");
			        SDSongBox.setText("");
			        SDAlbumBox.setText("");
			        SDYearBox.setText("");
				}
			} else {
				songList.remove(songPlayList.getSelectionModel().getSelectedIndex());
				songList.add(item);
				Collections.sort(songList, new sortSongName());
				int position = findIndex(songList, item);
				songs = FXCollections.observableList(songList);
				songPlayList.setItems(songs);
				songPlayList.getSelectionModel().select(position);
		        SDArtistBox.setText("");
		        SDSongBox.setText("");
		        SDAlbumBox.setText("");
		        SDYearBox.setText("");
			}
		}
		
	 /* The findDuplicate Method purpose is to find a duplicate song and return true if found and return false if there isn't a duplicate song. */
		private boolean findDuplicate(ArrayList<Song> songs, Song item) {
			boolean position = false;
			for(int i = 0; i<songs.size(); i++) {
				if(item.equals(songs.get(i))){
						position = true;
						return position;
				} 
			}
			return position;
		}
		
	 /* The findDuplicatePt2 Method purpose is to find a duplicate song and return true if found and return false if there isn't a duplicate song. */
		private boolean findDuplicatePt2(ArrayList<Song> songs, Song item) {
			boolean position = false;
			for(int i = 0; i<songs.size(); i++) {
				if(item.equalsPt2(songs.get(i))){
						position = true;
						return position;
				} 
			}
			return position;
		}
	
	/*
	 * ---------------------------
	 * deleteButtonAction Method
	 * 
	 * Deletes the selected song from the playlist
	 * ----------------------------
	 */
	public void deleteButtonAction(ActionEvent event) {
		int selectedIndex = songPlayList.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			Song songToRemove = (Song) songPlayList.getSelectionModel().getSelectedItem();
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to remove " + songToRemove.getName() + " by " + songToRemove.getArtist() + " from your library?", yes, ButtonType.CANCEL);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == yes) {
				int newSelectedIndex = 
						(selectedIndex == songPlayList.getItems().size() - 1)
							? selectedIndex - 1
							: selectedIndex;

					songs.remove(selectedIndex);		
					songPlayList.getSelectionModel().select(newSelectedIndex);
					Song newDisplay = (Song) songPlayList.getSelectionModel().getSelectedItem();
					if (selectedIndex != 0) {
	                    SDArtistBox.setText(newDisplay.getArtist());
	                    SDSongBox.setText(newDisplay.getName());
	                    SDAlbumBox.setText(newDisplay.getAlbum());
	                    SDYearBox.setText(newDisplay.getYear());
					}
					else {
						SDArtistBox.setText("");
						SDSongBox.setText("");
						SDAlbumBox.setText("");
						SDYearBox.setText("");
					}
			}
		}
	}
	
	/*
	 * ----------------------------------------
	 * selectButtonAction Method
	 * 
	 * Display the details of the selected song
	 * ----------------------------------------
	 */
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
	
	private void showSongDetails(Stage mainstage) {
		Song selected = (Song) songPlayList.getSelectionModel().getSelectedItem();
		SDArtistBox.setText(selected.getArtist());
		SDSongBox.setText(selected.getName());
		SDAlbumBox.setText(selected.getAlbum());
		SDYearBox.setText(selected.getYear());
		
	}

	/*
	 * -----------------------------------------------------------------
	 * Start Method
	 * 
	 * Initiates starting features that will occur when the GUI boots up
	 * ------------------------------------------------------------------
	 */
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
		//songPlayList
		//.getSelectionModel()
		//.selectedIndexProperty()
		//.addListener(obs, oldVal,newVal) -> showSongDetails(primaryStage);

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