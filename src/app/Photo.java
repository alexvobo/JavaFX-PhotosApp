package app;

import java.io.Serializable;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Calendar;

public class Photo implements Serializable {

	private transient Image img;
	private String name;
	private String location; //stock or one of the users
	private String caption;
	private String extension;
	private Calendar dateTime;
	private ArrayList<Tags> tags;

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Photo() {
		this.dateTime = Calendar.getInstance();
		this.dateTime.set(Calendar.MILLISECOND,0);
		this.tags = new ArrayList<Tags>();
	}


	/**
	 * @return the img
	 */
	public Image getImg() {
		return img;
	}
	/**
	 * @param img the img to set
	 */
	public void setImg(Image img) {
		this.img = img;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return extension;
	}
	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.extension = fileType;
	}
	/**
	 * @return the dateTime
	 */
	public Calendar getDateTime() {
		return dateTime;
	}
	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}
	/**
	 * @return the tags
	 */
	public ArrayList<Tags> getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(ArrayList<Tags> tags) {
		this.tags = tags;
	}

	/** Adds the tag to the tag list. If the tag conflicts with a tag that's already present it won't add it.
	 * @param tag tagName/tagValue to add
	 * @return Tags returns new tag
	 */
	public Tags addTag(String tagName, String tagValue) {
		if (tagName != null && tagValue != null) {
			for (int i = 0; i < getTags().size(); i++) {
				if (getTags().get(i).getTagName().toLowerCase() == tagName.toLowerCase() && 
						getTags().get(i).getTagValue().toLowerCase() == tagValue.toLowerCase()) {
					return null;
				}
			}
			Tags newTag = new Tags(tagName,tagValue);
			getTags().add(newTag);
			return newTag;
		}
		return null;
		
	}
	/** Removes the tag from the tag list
	 * @param tagName tagName to remove
	 * @param tagValue tagValue to remove
	 */
	public int removeTag(String tagName, String tagValue) {
		if (tagName != null && tagValue != null) {
			for (int i = 0; i < getTags().size(); i++) {
				if (getTags().get(i).getTagName().toLowerCase() == tagName.toLowerCase() && 
						getTags().get(i).getTagValue().toLowerCase() == tagValue.toLowerCase()) {
					getTags().remove(i);
					return i;
				}
			}
		}
		return -1;

	}
}
