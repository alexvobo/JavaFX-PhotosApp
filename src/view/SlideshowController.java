/**
 * 
 */
package view;

import java.util.ArrayList;

import app.Album;
import app.Photo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SlideshowController {
	@FXML Button nextButton;
	@FXML Button prevButton;
	@FXML Button closeButton;
	@FXML Label captionLabel;
	@FXML ImageView imgView;
	
	/**
	 * album to store
	 */
	Album album;
	/**
	 * photo in album to store
	 */
	Photo curPhoto;
	/**
	 * photo index to keep track of
	 */
	int curIndex;
	/**
	 * album size to store
	 */
	int albumSize;
	/**
	 * album photo array
	 */
	ArrayList<Photo> photoList;
	Image img;
	/**
	 * initializes variables and loads first photo in album
	 * @param mainStage stage that loads
	 * @param album album to load
	 */
	public void start(Stage mainStage, Album album) {
		this.album = album;
		this.curIndex = 0;
		this.photoList = album.getPhotos();
		this.albumSize = photoList.size();
		if (albumSize > 0) {
			load();
		}
	}
	/**
	 * gets photo from album based on curIndex and sets the ImageView in FXML
	 */
	public void load() {
		curPhoto = photoList.get(curIndex);
		captionLabel.setText(curPhoto.getCaption());
		img = new Image(curPhoto.getLocation());
		imgView.setImage(img);
	}
	/**
	 * Increases index, loads new photo as long as constraints allow
	 * @param e on event (click), do this method.
	 */
	public void nextImg(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == nextButton) {
			if(albumSize > 0 && curIndex+1 < albumSize) {
				curIndex++;
				load();
			}

		}
	}
	/**
	 * Decreases index, loads new photo as long as constraints allow
	 * @param e on event (click), do this method.
	 */
	public void prevImg(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == prevButton) {
			if(albumSize > 0 && curIndex-1 >= 0) {
				curIndex--;
				load();
			}

		}
	}
	/**
	 * Close button closes window.
	 * @param e on event (click), do this method.
	 */
	public void close(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == closeButton) {
			((Stage)(((Button)e.getSource()).getScene().getWindow())).close();

		}
	}
}
