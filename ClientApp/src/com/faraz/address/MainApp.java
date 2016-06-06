package com.faraz.address;

import java.io.IOException;

import com.faraz.address.model.ActivityModel;
import com.faraz.address.model.HeadFileModel;
import com.faraz.address.model.ProjectModel;
import com.faraz.address.model.VersioningModel;
import com.faraz.address.view.FileHistoryViewController;
import com.faraz.address.view.LoginController;
import com.faraz.address.view.ProgrammerController;
import com.faraz.address.view.ProjectViewController;
import com.faraz.address.view.UpdateViewController;
import com.faraz.address.view.newFileViewController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Server server;

	private ObservableList<ProjectModel> personProjectsData = FXCollections.observableArrayList();
	private ObservableList<ProjectModel> searchProjectsData = FXCollections.observableArrayList();

	private ObservableList<HeadFileModel> fileData = FXCollections.observableArrayList();
	private ObservableList<VersioningModel> versionData = FXCollections.observableArrayList();

	private ObservableList<ActivityModel> fileHistory = FXCollections.observableArrayList();

	public MainApp(){

	}

	public ObservableList<ActivityModel> getFileHistory(){
		return fileHistory;
	}

	public ObservableList<ProjectModel> getPersonProjectsData(){
		return personProjectsData;
	}

	public ObservableList<ProjectModel> getSearchProjectsData(){
		return searchProjectsData;
	}

	public ObservableList<HeadFileModel> getFileData(){
		return fileData;
	}

	public ObservableList<VersioningModel> getVersionData(){
		return versionData;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Farazin");

		System.out.println("Thread running ...");
		this.server = new Server();
		new Thread(server).start();
		System.out.println("Done");

		showLogin();
	}

	public void newFileWindow(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/newFileView.fxml"));
			AnchorPane page = loader.load();

			Scene sc = new Scene(page);
			Stage dialog = new Stage();
			dialog.setScene(sc);
			dialog.show();

			newFileViewController controller = loader.getController();
			controller.setMain(this, dialog);
		} catch (IOException e) {
			System.out.println("FXML faield");
		}
	}

	public void fileHistoryWindow(String name){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/FileHistoryView.fxml"));
			AnchorPane page = loader.load();

			Scene sc = new Scene(page);
			Stage dialog = new Stage();
			dialog.setScene(sc);
			dialog.show();

			FileHistoryViewController controller = loader.getController();
			controller.setMain(this, dialog, name);
		} catch (IOException e) {
			System.out.println("FXML faield");
			e.printStackTrace();
		}
	}

	public void updateWindow(String name, String version){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/UpdateView.fxml"));
			AnchorPane page = loader.load();

			Scene sc = new Scene(page);
			Stage dialog = new Stage();
			dialog.setScene(sc);
			dialog.show();

			UpdateViewController controller = loader.getController();
			controller.setMain(this, dialog, name, version);
		} catch (IOException e) {
			System.out.println("FXML faield");
		}
	}

	public void ProjectView(){
		try {
			primaryStage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ProjectViewRoot.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();

			ProjectViewController controller = loader.getController();
			controller.setMain(this);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FXML not load");
		}
	}

	public void ProgrammerView(){
		try {
			primaryStage.close();

			FXMLLoader loader = new FXMLLoader();
			//loader.setLocation(MainApp.class.getResource("view/LoginView.fxml"));
			loader.setLocation(MainApp.class.getResource("view/Programmer.fxml"));
			AnchorPane ProgrammerPage = (AnchorPane) loader.load();

			Scene scene = new Scene(ProgrammerPage);
			primaryStage.setScene(scene);
			primaryStage.show();

			ProgrammerController controller = loader.getController();
			controller.setMain(this);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FXML load Failed");
		}
	}

	public void showLogin(){
		try {
			primaryStage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/LoginView.fxml"));
			//loader.setLocation(MainApp.class.getResource("view/Programmer.fxml"));
			AnchorPane loginPage = (AnchorPane) loader.load();

			Scene scene = new Scene(loginPage);
			primaryStage.setScene(scene);
			primaryStage.show();

			LoginController controller = loader.getController();
			controller.setMain(this);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FXML load Failed");
		}
	}

	public Server getServer(){
		return this.server;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
