package app;

public class Song {
	String name, artist, album; int year;
	
	public Song(String name, String artist, String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String getName() {return name;}
	public String getArtist() {return artist;}
	public String getAlbum() {return album;}
	public int getYear() {return year;}
	
	public void setName(String name) {this.name=name;}
	public void setArtist(String artist) {this.artist=artist;}
	public void setAlbum(String album) {this.album=album;}
	public void setYear(int year) {this.year=year;}
	
	public String toString() {
		return name + "," + artist + "," + album + "," + year;
	}
}