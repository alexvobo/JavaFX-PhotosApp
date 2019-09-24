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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.IOStreamController;

public class EditTagController {
	@FXML Button addButton;
	@FXML Button deleteButton;
	@FXML Button closeButton;
	@FXML Label captionLabel;
	@FXML TextField tagField1;
	@FXML TextField tagField2;

	@FXML ListView<Tags> tagListView;
	static ObservableList<Tags> obsList;
	ArrayList<User> usrList;
	ArrayList<Tags> tagList;
	Stage mainStage;

	int albumIndex;
	int photoIndex;
	int numTags;
	int usrIndex;
	Photo photo;
	/**
	 * Initialize label and load tag listview
	 * @param usrIndex index of user
	 * @param photoIndex index of photo we are retrieving
	 * @param albumIndex index of album that photo is in
	 * @throws IOException 
	 */
	public void start(Stage mainStage, int usrIndex, int photoIndex, int albumIndex) throws IOException {
		this.photoIndex = photoIndex;
		this.albumIndex= albumIndex;
		this.usrIndex = usrIndex;
		usrList = IOStreamController.read();
		obsList = FXCollections.observableArrayList();
		photo =  usrList.get(usrIndex).getAlbum(albumIndex).getPhoto(photoIndex);
		numTags =  photo.getTags().size();
		captionLabel.setText(photo.getCaption());

		for(int i = 0; i < numTags; i++) {
			obsList.add(photo.getTags().get(i));
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

		tagListView
		// set listener for the listview clicks
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				System.out.println("test"));
	}
	/**
	 * add tag to photo's tag list
	 * @param e on button click
	 * @throws IOException on output error
	 */
	public void addTag(ActionEvent e) throws IOException {
		String tagName, tagValue;
		Tags newTag;
		Button b = (Button)e.getSource();
		if (b == addButton) {
			tagName = tagField1.getText();
			tagValue = tagField2.getText();
			if (tagName.length() == 0 || tagValue.length()==0) {
				Alert existAlert = new Alert(AlertType.ERROR);
				existAlert.setTitle("Empty Fields");
				existAlert.setContentText("Both tagName and tagValue are required");
				existAlert.showAndWait();
			} else {
				if ((newTag = usrList.get(usrIndex).getAlbum(albumIndex).getPhoto(photoIndex).addTag(tagName, tagValue)) == null) {
					Alert existAlert = new Alert(AlertType.ERROR);
					existAlert.setTitle("Tag already exists");
					existAlert.setContentText("The tags you chose already exist in the list");
					existAlert.showAndWait();
					return;
				} else {
					obsList.add(newTag);
					tagListView.setItems(obsList);
					highlightTag(newTag);
					IOStreamController.write(usrList);
				}
			}

		}
	}
	/**
	 * add tag to photo's tag list
	 * @param e on button click
	 * @throws IOException on output error
	 */
	public void deleteTag(ActionEvent e) throws IOException {
		String tagName, tagValue;
		int index;
		Button b = (Button)e.getSource();
		if (b == deleteButton) {
			tagName = tagField1.getText();
			tagValue = tagField2.getText();
			if (tagName.length() == 0 || tagValue.length()==0) {
				Alert existAlert = new Alert(AlertType.ERROR);
				existAlert.setTitle("Empty Fields");
				existAlert.setContentText("Both tagName and tagValue are required");
				existAlert.showAndWait();
				return;
			} else {
				if ((index = usrList.get(usrIndex).getAlbum(albumIndex).getPhoto(photoIndex).removeTag(tagName, tagValue))==-1) {
					Alert existAlert = new Alert(AlertType.ERROR);
					existAlert.setTitle("Tag Not Found");
					existAlert.setContentText("Both tagName and tagValue are required");
					existAlert.showAndWait();
				} else {
					obsList.remove(index);
					tagListView.setItems(obsList);
					tagListView.getSelectionModel().select(0);
					tagField1.setText(obsList.get(index).getTagName());
					tagField2.setText(obsList.get(index).getTagValue());
					IOStreamController.write(usrList);
				}
			}

		}
	}
	/**
	 * highlight tag in list view
	 * @param tag tag to highlight
	 */
	public void highlightTag(Tags tag) {
		int index = 0;
		for(Tags i: obsList)
		{
			if(i == tag)
			{
				tagListView.getSelectionModel().select(index);
				tagField1.setText(i.getTagName());
				tagField2.setText(i.getTagValue());
				break;
			}
			index++;
		}
	}
	/**
	 * close edit tag window
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
