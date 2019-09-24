package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.Album;
import app.Photo;
import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.IOStreamController;

public class AlbumController {
	
	
	@FXML Button addPhotoButton;
	@FXML Button captionPhotoButton;
	@FXML Button deletePhotoButton;
	@FXML Button slideShowButton;
	@FXML Button editTagButton;
	@FXML Button logoutButton;
	@FXML Button quitButton;
	@FXML Button displayButton;
	
	@FXML TextField addPhotoText;
	@FXML TextField captionPhotoText;
	
	@FXML ListView<Photo> photoList;
	static ObservableList<Photo> obsList;
	
	private ArrayList<Photo> photoAlbum = new ArrayList<Photo>();
	private ArrayList<User> usrList = new ArrayList<User>();
	
	private Stage openStage;
	private Album album;
	private int usrIndex;
	
	/**
	 * Sets up the initial photos in the album
	 * @param album
	 * 
	 */
	public void displayAlbum(Album album, Stage openStage) throws IOException{
		this.openStage = openStage;
		this.album = album;
		//this.usrIndex = usrIndex;
		obsList = FXCollections.observableArrayList();
		
		photoAlbum = album.getPhotos();
		for(int i = 0; i < photoAlbum.size(); i++) 
			obsList.add(photoAlbum.get(i));		
		
		photoList.setItems(obsList);
		
		photoList.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			ImageView img = new ImageView();
			@Override
			public ListCell<Photo> call(ListView<Photo> p) {
				ListCell<Photo> cell = new ListCell<Photo>(){
					@Override
					protected void updateItem(Photo item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						}
						else
						{
							setText(item.getName());
							try {
								img.setImage(new Image(new FileInputStream(item.getLocation())));
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println(item.getLocation());
							img.setFitHeight(100);
							img.setFitWidth(100);
							setGraphic(img);
						}
					}
				
				};
				return cell;
			}
		});
		
		if (!obsList.isEmpty()) {
			photoList.getSelectionModel().select(0);
		} 	
		photoList
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				System.out.println("test"));
		
		
		
	}
	/**
	 * change the caption of a photo
	 * @param e
	 */
	public void changeCaption(ActionEvent e) {
		Button b = (Button)e.getSource();
		if(b == captionPhotoButton) {
			Photo p = photoList.getSelectionModel().getSelectedItem();
			p.setCaption(captionPhotoText.getText());
		}
		
	}
	@FXML
	public void deletePhoto(ActionEvent e) {
		Button b = (Button)e.getSource();
		if(b == deletePhotoButton) {
			System.out.println("deleteTest");
			Photo p = photoList.getSelectionModel().getSelectedItem();
			int index = retrievePos(p);
			

			Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
			confirmAlert.setTitle("Delete Photo");
			confirmAlert.setContentText("Are you sure you want to delete this photo?");
			
			Optional<ButtonType> result = confirmAlert.showAndWait();
			if (result.get() == ButtonType.OK){
				obsList.remove(index);
				photoList.setItems(obsList); 
				photoList.getSelectionModel().select(index);
				
				album.deletePhoto(p);
			}
			
		}
		
	}
	
	
	/**
	 * Retrieve position of photo on list
	 * @param song
	 * @return
	 */
	public int retrievePos(Photo photo) {
			int index = 0;
			for(Photo i: obsList)
			{
				if(i == photo)
				{
					break;
				}
				index++;
			}
			return index;
	}
	
	/**
	 * Add a new photo
	 * @param e
	 */
	@FXML
	public void addPhoto(ActionEvent e) {
		Button b = (Button)e.getSource();
		if(b == addPhotoButton){
			String path = addPhotoText.getText();
			Photo temp = new Photo();
			File f = new File(path);
			System.out.println(f.toURI().toString());
			temp.setImg(new Image(f.toURI().toString()));
			temp.setFileType(path.substring(path.indexOf(".")));
			temp.setCaption(path);
			temp.setName(path);
			album.addPhoto(temp);
			obsList.add(temp);
			photoList.setItems(obsList);
		}	
	}
	
	@FXML
	public void slideShow(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if(b == slideShowButton) {
			FXMLLoader slideLoader = new FXMLLoader();
			slideLoader.setLocation(getClass().getResource("/view/slideshow.fxml"));
			VBox root = (VBox)slideLoader.load();
			SlideshowController slideshowcontroller = slideLoader.getController();
			Scene slideScene = new Scene(root);
			
			Stage slideStage = new Stage();
			slideStage.setScene(slideScene);
			slideStage.setTitle("Photo Library");
			slideStage.setResizable(false);
			
		
			slideshowcontroller.start(slideStage, album);
			slideStage.show();
			openStage.close();
			
		}
		
	}
	@FXML
	public void displayPhoto(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if(b == displayButton) {
			Photo p = photoList.getSelectionModel().getSelectedItem();

			FXMLLoader displayLoader = new FXMLLoader();
			displayLoader.setLocation(getClass().getResource("/view/ImageDisplay.fxml"));
			VBox root = (VBox)displayLoader.load();
			DisplayController displayController = displayLoader.getController();
			Scene displayScene = new Scene(root);
			
			Stage displayStage = new Stage();
			displayStage.setScene(displayScene);
			displayStage.setTitle("Photo Library");
			displayStage.setResizable(false);
			displayStage.show();
			displayController.start(displayStage, p);
			openStage.close();
			
		}
	}
	@FXML
	public void startSearch(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if(b == slideShowButton) {
			FXMLLoader searchLoader = new FXMLLoader();
			searchLoader.setLocation(getClass().getResource("/view/slideshow.fxml"));
			VBox root = (VBox)searchLoader.load();
			SearchController searchcontroller = searchLoader.getController();
			Scene searchScene = new Scene(root);
			
		}
	}
	
	@FXML
	public void editTags(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if(b == editTagButton) {
			
			FXMLLoader editLoader = new FXMLLoader();
			editLoader.setLocation(getClass().getResource("/view/edittag.fxml"));
			VBox root = (VBox)editLoader.load();
			EditTagController edittagcontroller = editLoader.getController();
			Scene editScene = new Scene(root);
			
			Stage editStage = new Stage();
			editStage.setScene(editScene);
			editStage.setTitle("Photo Library");
			editStage.setResizable(false);
		
			
			Photo p = photoList.getSelectionModel().getSelectedItem();
			
			usrList = IOStreamController.read();
			ArrayList<Album> usrCollection = usrList.get(usrIndex).getPhotoAlbum();
			int albumIndex = usrCollection.indexOf(album);
		
			edittagcontroller.start(editStage, usrIndex, retrievePos(p), albumIndex);;
			editStage.show();
			openStage.close();
			
		}
		
	}
	

	/**
	 * be brought back to the login screen
	 * @param e
	 * @throws IOException
	 */
	public void logout(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if (b == logoutButton) {
			IOStreamController.write(usrList);
			FXMLLoader loginLoader = new FXMLLoader();
			loginLoader.setLocation(getClass().getResource("/view/Login.fxml"));
			AnchorPane root = (AnchorPane)loginLoader.load();
			LoginController loginController = loginLoader.getController();
			Scene loginScene = new Scene(root);
			Stage loginStage = new Stage();
			loginStage.setScene(loginScene);
			loginStage.setTitle("Photo Library");
			loginStage.setResizable(false);
			loginStage.show();
			((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
			loginController.start(loginStage);

		
	}
}

	/**
	 * quits program
	 * @throws IOException
	 */
	public void quit(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if(b == quitButton) {
			openStage.close();
		}
}

	
	
}
