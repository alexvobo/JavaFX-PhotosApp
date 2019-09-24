package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.IOStreamController;
import app.Album;
import app.Photo;
import app.User;


public class MainMenuController {
		
	@FXML Button newAlbum;
	@FXML Button deleteAlbum;
	@FXML Button openAlbum;
	@FXML Button renameAlbum;
	@FXML Button logoutButton;
	@FXML Button quitButton;
	
	@FXML TextField name;
	@FXML TextField range;
	@FXML TextField renameText;
	@FXML TextField number;
	
	@FXML ListView<Album> albumList;
	static ObservableList<Album> obsList;
	
	private ArrayList<User> usrList = new ArrayList<User>();
	private ArrayList<Album> usrCollection = new ArrayList<Album>();
	private User usr;
	private Stage mainStage;
	
	/**
	 * create the first album and place it in the main menu
	 * @throws IOException 
	 */
	public void startStock(Stage mainStage, int usrIndex) throws IOException {
		this.mainStage = mainStage;
		
		obsList = FXCollections.observableArrayList();
		usrList = IOStreamController.read();
		usr = usrList.get(usrIndex);
		
		usrCollection = usrList.get(usrIndex).getPhotoAlbum();
		for(int i=0; i < usrCollection.size(); i++) {
			obsList.add(usrCollection.get(i));
		}
		//obsList.add(stockAlbum);
		
		albumList.setItems(obsList);
		
		albumList.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>(){
			ImageView img = new ImageView();
			@Override
			public ListCell<Album> call(ListView<Album> p) {
				ListCell<Album> cell = new ListCell<Album>(){
					@Override
					protected void updateItem(Album item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null || item.getPhoto(0) == null) {
							setText(null);
							setGraphic(null);
						}
						else
						{
							setText(item.getName());
							img.setImage(new Image(item.getPhoto(0).getLocation()));
							img.setFitHeight(100);
							img.setFitWidth(100);
							setGraphic(img);
						}
					}
				
				};
				return cell;
			}
		});
		
		albumList
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				getAlbumInfo(mainStage));
		
		
		if (!obsList.isEmpty()) {
			albumList.getSelectionModel().select(0);
		} 

		
		mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent close) {
				try {
					IOStreamController.write(usrList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});
		
	}
	

	public void getAlbumInfo(Stage mainStage) {
		Album album = albumList.getSelectionModel().getSelectedItem();
		if(album != null)
				retrieveFieldData(album);
	}
	
	public void retrieveFieldData(Album album) {
		name.setText(album.getName());
		range.setText(album.getDates());
		number.setText(album.getCount());
		
	}
	
	/**
	 * Creates a untitled empty album
	 * @param e
	 * @throws IOException
	 */
	@FXML
	public void addAlbum(ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		if(b == newAlbum) {
			Album temp = new Album("untitled");
			obsList.add(temp);
			albumList.setItems(obsList);
			
			usr.newAlbum(temp);
		}
		
	}
	
	
	/**
	 * Delete selected album
	 * @param e
	 * @throws IOException
	 */
	@FXML 
	public void delete(ActionEvent e)throws IOException{
		Button b = (Button)e.getSource();
		if(b == deleteAlbum) {
			Album album = albumList.getSelectionModel().getSelectedItem();
			int index = retrievePos(album);
			
			Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
			confirmAlert.setTitle("Delete Album");
			confirmAlert.setContentText("Are you sure you want to delete this album?");
			
			Optional<ButtonType> result = confirmAlert.showAndWait();
			if (result.get() == ButtonType.OK){
				obsList.remove(index);
				albumList.setItems(obsList); 
				albumList.getSelectionModel().select(index);
				
				//remove the album from userlist
				usr.deleteAlbum(album);
			}
		}
		
	}
	
	
	/**
	 * Retrieve position of album on list
	 * @param song
	 * @return
	 */
	public int retrievePos(Album album) {
			int index = 0;
			for(Album i: obsList)
			{
				if(i == album)
				{
					break;
				}
				index++;
			}
			return index;
	}
	
	
	
	public void rename(ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		if(b == renameAlbum) {
			Album album = albumList.getSelectionModel().getSelectedItem();
			int index = retrievePos(album);
			String newName = renameText.getText().trim();
			
			for(Album i : obsList) {
				if(i.getName().compareToIgnoreCase(newName) == 0) {
					Alert existAlert = new Alert(AlertType.ERROR);
					existAlert.setTitle("Album Already Exists");
					existAlert.setContentText("The new album name you chose already exists in the list");
					existAlert.showAndWait();
					return;
					
				}
			}
			album.rename(newName);
			usr.renameAlbum(album, newName);
			obsList.set(index, album);
			albumList.setItems(obsList);
			
		}
	}
	
	@FXML
	public void openScreen(ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		if(b == openAlbum) {
			FXMLLoader openLoader = new FXMLLoader();
			openLoader.setLocation(getClass().getResource("/view/album.fxml"));
			VBox root = (VBox)openLoader.load();
			AlbumController albumcontroller = openLoader.getController();
			Scene openScene = new Scene(root);
			
			Stage openStage = new Stage();
			openStage.setScene(openScene);
			openStage.setTitle("Photo Library");
			openStage.setResizable(false);
			
			Album album = albumList.getSelectionModel().getSelectedItem();
		
			albumcontroller.displayAlbum(album, openStage);
			openStage.show();
			mainStage.close();
			
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
			IOStreamController.write(usrList);
			mainStage.close();
		}
	}
	
	
}
