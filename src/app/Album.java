package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javafx.scene.image.Image;

public class Album implements Serializable{
	private ArrayList<Photo> photos;
	private String name;
	
	public Album(String name){
		this.name = name;
		photos = new ArrayList<Photo>();
	}

	public Photo getPhoto(int index) {
		return this.photos.get(index);
	}
	
	public ArrayList<Photo> getPhotos() {
		return this.photos;
	}
	
	public String getName() {		
		return this.name;
	}

	public Image getThumbnail() {
		if(this.photos.isEmpty() == false) {
			return this.photos.get(0).getImg();
		}
		else {
			return null;
		}
	}
	
	public String getCount() {
		return new Integer(this.photos.size()).toString();
	}
	
	public String getDates() {
		if(this.photos.isEmpty() == true)
			return null;
		
		Collections.sort(this.photos, new DateLexicographicComparator());
		Date date1 = this.photos.get(0).getDateTime().getTime();
		Date date2 = this.photos.get(this.photos.size() - 1).getDateTime().getTime();
		
		return (date1.toString() + " - " + date2.toString());
	}
	
	
	public class DateLexicographicComparator implements Comparator<Photo>{
		@Override
		public int compare(Photo a, Photo b) {
			return a.getDateTime().compareTo(b.getDateTime());
		}
		
	}
	
	public void rename(String newName) {
		this.name = newName;
	}
	
	
	
	
	public void addPhoto(Photo photo) {
		photos.add(photo);
	}
	
}
