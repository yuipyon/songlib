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

	public String toString() {
		return name + "," + artist + "," + album + "," + year;
	}
}