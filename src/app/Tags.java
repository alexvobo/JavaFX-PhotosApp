package app;

import java.io.Serializable;

public class Tags implements Serializable {
	private String tagName;
	private String tagValue;
	
	public Tags(String tagName, String tagValue) {
		this.tagName = tagName;
		this.tagValue = tagValue;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	public String toString() {
		return  String.format("(%s, %s)", this.tagName,this.tagValue);
	}
	
}
