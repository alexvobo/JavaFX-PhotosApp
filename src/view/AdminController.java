package view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.IOStreamController;


public class AdminController implements Serializable {
	@FXML ListView<User> usrListView;
	@FXML Button createButton;
	@FXML Button deleteButton;
	@FXML Button logoutButton;
	//ObservableList <User> userList = new ArrayList<User>();

	Stage primaryStage;
	ArrayList<User> usrList = new ArrayList<User>();
	ObservableList<User> obsList;

	public void start(Stage adminStage) throws IOException {
		usrList = IOStreamController.read();
		obsList = FXCollections.observableArrayList();
		for(int i = 0; i < usrList.size(); i++) {
			System.out.println(usrList.get(i).getUserName());
			obsList.add(usrList.get(i));
		}
		usrListView.setItems(obsList);
		
		usrListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>(){
			@Override
			public ListCell<User> call(ListView<User> p) {
				ListCell<User> cell = new ListCell<User>(){
					@Override
					protected void updateItem(User item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
						}
						else
						{
							setText(item.getUserName());
						}
					}
				};
				return cell;
			}
		});
		
		if (!obsList.isEmpty()) {
			usrListView.getSelectionModel().select(0);
		}
		
		usrListView
		// set listener for the listview clicks
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				System.out.println("test"));
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent close) {
				try {
					IOStreamController.write(usrList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}});
	}
	public int retrievePos(User usr) {
		int index = 0;
		for(User i: obsList)
		{
			if(i == usr)
			{
				break;
			}
			index++;
		}
		return index;
	}
	public void highlightUser(User usr) {
		int index = 0;
		for(User i: obsList)
		{
			if(i == usr)
			{
				usrListView.getSelectionModel().select(index);
				break;
			}
			index++;
		}
	}

	public void createUser(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == createButton) {
			TextInputDialog dialog = new TextInputDialog("");
			 
			dialog.setTitle("Create User");
			dialog.setHeaderText("Enter username: ");
			dialog.setContentText("Username:");
			 
			Optional<String> result = dialog.showAndWait();
			 
			result.ifPresent(name -> {
				if (name.length()> 0) {
					for(User i: obsList){
						if(i.getUserName().compareToIgnoreCase(name) == 0) {
							Alert existAlert = new Alert(AlertType.ERROR);
							existAlert.setTitle("User Already Exists");
							existAlert.setContentText("The username you chose already exists in the list");
							existAlert.showAndWait();
							return;
						}
					}
					User newUsr = new User(name);
					obsList.add(newUsr);
					usrList.add(newUsr);
					usrListView.setItems(obsList);
					highlightUser(newUsr);
					try {
						IOStreamController.write(usrList);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
	}
	public void deleteUser(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		if (b == deleteButton) {
			User usr = usrListView.getSelectionModel().getSelectedItem();
			int index = retrievePos(usr);

			Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
			confirmAlert.setTitle("Delete User");
			confirmAlert.setContentText("Are you sure you want to delete this user?");

			Optional<ButtonType> result = confirmAlert.showAndWait();
			if (result.get() == ButtonType.OK){
				obsList.remove(index);
				usrList.remove(usrList.indexOf(usr));
				//FXCollections.sort(obsList,new User());
				usrListView.setItems(obsList); 
				usrListView.getSelectionModel().select(index);
			}
			IOStreamController.write(usrList);
		}
	}
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
	
}
