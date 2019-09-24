package view;

import java.io.IOException;
import java.util.ArrayList;

import app.Album;
import app.Photo;
import app.Tags;
import app.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.IOStreamController;
import view.MainMenuController;
/**
 * Controller class for the Login page
 */
public class LoginController {

	@FXML Button loginButton;
	@FXML Button quitButton;
	@FXML TextField userName;
	public Stage menuStage;
	public Stage primaryStage;
	public Stage adminStage;
	private ArrayList<User> usrList = new ArrayList<User>();

	public void start(Stage primaryStage) throws IOException{
		this.primaryStage = primaryStage;

		try {
			usrList = IOStreamController.read();
		} finally {
			boolean stockLoaded = false;
			for (int i = 0; i < usrList.size();i++) {
				if (usrList.get(i).getUserName().toLowerCase().equals("stock")) {
					stockLoaded = true;
				}
			}

			if (!stockLoaded) {
				stock();
				System.out.println("STOCK LOADED");
				IOStreamController.write(usrList);
			}
		}

		/**
		 *	Sentinel for checking if stock user exists
		 */


		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent close) {
				try {
					IOStreamController.write(usrList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});
	}


	/**
	 * Loads user in based on credentials.
	 * @param e Called via button press
	 * @return Nothing
	 * @exception IOException On write error.
	 */
	@FXML
	public void authenticate(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();

		if(b == loginButton) {
			String usr = userName.getText().toLowerCase();
			if (usr.length() == 0) {
				Alert existAlert = new Alert(AlertType.ERROR);
				existAlert.setTitle("Username ERROR");
				existAlert.setContentText("Username must be longer than 0 characters");
				existAlert.showAndWait();
			} else if (usr.equals("admin")){
				FXMLLoader adminLoader = new FXMLLoader();
				adminLoader.setLocation(getClass().getResource("/view/Admin.fxml"));
				AnchorPane root = (AnchorPane)adminLoader.load();
				AdminController adminController = adminLoader.getController();
				Scene adminScene = new Scene(root);
				adminStage = new Stage();
				adminStage.setScene(adminScene);
				adminStage.setTitle("Admin Page");
				adminStage.setResizable(false);
				adminStage.show();
				primaryStage.close();
				adminController.start(adminStage);
			} else {
				int usrIndex = -1;
				for (int i = 0; i < usrList.size();i++) {
					if (usrList.get(i).getUserName().equals(usr)) {
						usrIndex = i;
					} else {
						User newUsr = new User(usr);
						usrList.add(newUsr);
						usrIndex = usrList.indexOf(newUsr);
					}
				}
				if (usrIndex != -1) {
					IOStreamController.write(usrList);

					FXMLLoader menuLoader = new FXMLLoader();
					menuLoader.setLocation(getClass().getResource("/view/MainMenu.fxml"));
					VBox root = (VBox)menuLoader.load();
					MainMenuController maincontroller = menuLoader.getController();
					Scene menuScene = new Scene(root);
					menuStage = new Stage();
					menuStage.setScene(menuScene);
					menuStage.setTitle("Photo Library");
					menuStage.setResizable(false);
					menuStage.show();
					primaryStage.close();
					maincontroller.startStock(menuStage,usrIndex);
				}
			}
		}
	}

	/**
	 * Creates stock photo user.
	 * @return Nothing
	 * @exception IOException On write error.
	 */
	public void stock() throws IOException {
		User newUsr = new User("stock");
		Album album = new Album("Stock Photos");

		Photo bitcoin = new Photo();
		bitcoin.setImg(new Image("file:btc.png"));
		bitcoin.setName("Bitcoin");
		bitcoin.setCaption("Magic Internet Money");
		bitcoin.setLocation("stockPhotos/btc.png");
		bitcoin.setFileType(".png");
		ArrayList<Tags> bitcoinTags = new ArrayList<Tags>();
		bitcoinTags.add(new Tags("magic", "money"));
		bitcoin.setTags(bitcoinTags);
		album.addPhoto(bitcoin);

		Photo butterfly = new Photo();
		butterfly.setImg(new Image("file:butterfly.jpg"));
		butterfly.setName("Butterfly");
		butterfly.setCaption("Don't you just love nature?");
		butterfly.setLocation("stockPhotos/butterfly.jpg");
		butterfly.setFileType(".jpg");
		ArrayList<Tags> butterflyTags = new ArrayList<Tags>();
		butterflyTags.add(new Tags("Kingdom", "Animalia"));
		butterflyTags.add(new Tags("class", "Insecta"));
		butterfly.setTags(butterflyTags);
		album.addPhoto(butterfly);


		Photo student = new Photo();
		student.setImg(new Image("file:CollegeStudent.jpg"));
		student.setName("College Student");
		student.setCaption("I'm 21 and feel great!");
		student.setLocation("stockPhotos/CollegeStudent.jpg");
		student.setFileType(".jpg");
		ArrayList<Tags> studentTags = new ArrayList<Tags>();
		studentTags.add(new Tags("College", "Rutgers"));
		studentTags.add(new Tags("major", "computer science"));
		student.setTags(studentTags);
		album.addPhoto(student);


		Photo moon = new Photo();
		moon.setImg(new Image("file:moon.jpg"));
		moon.setName("Moon");
		moon.setCaption("When moon?");
		moon.setLocation("stockPhotos/moon.jpg");
		moon.setFileType(".jpg");
		ArrayList<Tags> moonTags = new ArrayList<Tags>();
		moonTags.add(new Tags("size", "big"));
		moonTags.add(new Tags("physical location", "space"));
		moon.setTags(moonTags);
		album.addPhoto(moon);

		Photo panda = new Photo();
		panda.setImg(new Image("file:panda.jpg"));
		panda.setName("Panda");
		panda.setCaption("Black and White");
		panda.setLocation("stockPhotos/panda.jpg");
		panda.setFileType(".jpg");
		ArrayList<Tags> pandaTags = new ArrayList<Tags>();
		pandaTags.add(new Tags("Vicious?", "very"));
		pandaTags.add(new Tags("favorite food", "bamboo"));
		panda.setTags(pandaTags);
		album.addPhoto(panda);

		newUsr.newAlbum(album);
		usrList.add(newUsr);
	}
	/**
	 * Saves and closes stage.
	 * @return Nothing
	 * @exception IOException On write error.
	 */
	public void quit() throws IOException {
		IOStreamController.write(usrList);
		primaryStage.close();
	}
}
