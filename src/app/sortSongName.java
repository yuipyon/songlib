package app;

import java.util.Comparator;

public class sortSongName implements Comparator<Song> 
{  
    public int compare(Song a, Song b) 
    { 
        return a.getName().compareTo(b.getName()); 
    } 
}
