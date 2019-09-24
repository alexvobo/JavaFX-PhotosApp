/**
 * 
 */
package view;

import java.io.IOException;
import java.util.ArrayList;

import app.Photo;
import app.Tags;
import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.IOStreamController;

public class DisplayController {
	@FXML Button closeButton;
	@FXML Label captionLabel;
	@FXML Label timeStampLabel;
	@FXML ImageView imgView;
	@FXML ListView<Tags> tagListView;
	static ObservableList<Tags> obsList;
	ArrayList<User> usrList;
	ArrayList<Tags> tagList;
	Stage mainStage;

	int numTags;
	Image img;
	/**
	 * Initialize label and load tag listview
	 * @param usrIndex index of user
	 * @param photoIndex index of photo we are retrieving
	 * @param albumIndex index of album that photo is in
	 * @throws IOException 
	 */
	public void start(Stage mainStage, Photo p) throws IOException {

		usrList = IOStreamController.read();
		obsList = FXCollections.observableArrayList();
		
		numTags =  p.getTags().size();
		captionLabel.setText(p.getCaption());
		timeStampLabel.setText(p.getDateTime().getTime().toString());
		img = new Image(p.getLocation());
		imgView.setImage(img);
		
		for(int i = 0; i < numTags; i++) {
			obsList.add(p.getTags().get(i));
		}

		tagListView.setItems(obsList);

		tagListView.setCellFactory(new Callback<ListView<Tags>, ListCell<Tags>>(){
			@Override
			public ListCell<Tags> call(ListView<Tags> p) {
				ListCell<Tags> cell = new ListCell<Tags>(){
					@Override
					protected void updateItem(Tags item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
						}
						else
						{
							setText(item.getTagName() + ", " + item.getTagValue());
						}
					}
				};
				return cell;
			}
		});

		if (!obsList.isEmpty()) {
			tagListView.getSelectionModel().select(0);
		}

//		tagListView
//		// set listener for the listview clicks
//		.getSelectionModel()
//		.selectedIndexProperty()
//		.addListener(
//				(obs, oldVal, newVal) -> 
//				System.out.println("test"));
	}

	/**
	 * close display photo window
	 * @param e on button click
	 * @throws IOException on output error
	 */
	public void close(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if (b == closeButton) {
			IOStreamController.write(usrList);
			((Stage)(((Button)e.getSource()).getScene().getWindow())).close();

		}
	}
}
