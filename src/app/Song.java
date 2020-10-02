/*
 * @author: Yulin Ni (yn140) 
 * @author: Karun Kanda (kk951)
 * @version: 1.0
 */

package app;

/*
 * ----------------------------------------------------------------------------
 * 
 * Song Class
 * 
 * Description: Object that contains the individual data of the song the user wants to store.
 * 
 * ----------------------------------------------------------------------------
 */

public class Song {
	
	String name, artist, album, year;
	
	public Song(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String getName() {return name;}
	public String getArtist() {return artist;}
	public String getAlbum() {return album;}
	public String getYear() {return year;}
	
	public void setName(String name) {this.name=name;}
	public void setArtist(String artist) {this.artist=artist;}
	public void setAlbum(String album) {this.album=album;}
	public void setYear(String year) {this.year=year;}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
	         return false;
	    }
		
		if(this == o) {
	         return true;
	    }
		
		if(getClass() != o.getClass()) {
			return false;
		}
		
		Song song = (Song)o;
		return name.equals(song.name) && artist.equals(song.artist);
	}
		
	public void edit(String name, String artist, String album, String year) {
		this.artist = artist;
		this.name = name;
		this.album = album;
		this.year = year;
	}
	
	public String toString() {
		return artist + " " + "-" + " " + name;
	}
}
