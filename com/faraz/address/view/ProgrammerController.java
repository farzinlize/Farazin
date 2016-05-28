package com.faraz.address.view;

import java.io.IOException;

import com.faraz.address.MainApp;
import com.faraz.address.model.ProjectModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ProgrammerController {
	@FXML
	private AnchorPane mainPane;
	@FXML
	private TabPane tabs;
	@FXML
	private Tab openTab;
	@FXML
	private Tab joinTab;
	@FXML
	private Tab newTab;
	@FXML
	private AnchorPane openPane;
	@FXML
	private TableView<ProjectModel> openTable;
	@FXML
	private TableColumn<ProjectModel, String> openNameCol;
	@FXML
	private TableColumn<ProjectModel, String> openDetailsCol;
	@FXML
	private Button openButton;
	@FXML
	private Label yourProjlbl;
	@FXML
	private AnchorPane joinPane;
	@FXML
	private PasswordField PassBar;
	@FXML
	private Button joinButton;
	@FXML
	private TextField projNameBar;
	@FXML
	private TableView<ProjectModel> joinTable;
	@FXML
	private TableColumn<ProjectModel, String> joinNameCol;
	@FXML
	private TableColumn<ProjectModel, String> joinDetailsCol;
	@FXML
	private Label passLbl;
	@FXML
	private AnchorPane newPane;
	@FXML
	private TextField newProjNameBar;
	@FXML
	private Button checkName;
	@FXML
	private Button newCreate;
	@FXML
	private Label checkResult;
	@FXML
	private CheckBox publicOrPrivate;
	@FXML
	private Label passMatchResult;
	@FXML
	private PasswordField projPassBar;
	@FXML
	private PasswordField projPassBar2;
	@FXML
	private Label creatingResult;
	@FXML
	private Button refreshInOpen;
	@FXML
	private Button refreshInJoin;
	@FXML
	private TextArea newProjDetail;
	@FXML
	private Label joinResult;

	private MainApp main;

	public void setMain(MainApp main){
		this.main = main;
		openTable.setItems(main.getPersonProjectsData());
		joinTable.setItems(main.getSearchProjectsData());
	}

	@FXML
	public void openProject() {
		try {
			this.main.getServer().out.writeUTF("openProject");
			String PN = openTable.getSelectionModel().getSelectedItem().getName();
			if(PN.equals("")){
				System.out.println("ERROR");
				return ;
			}
			this.main.getServer().out.writeUTF(PN);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("ERROR")){

			}
			else if(msg.equals("OK")){
				System.out.println("Its good");
				this.main.ProjectView();
			}
			else {
				System.out.println(msg);
			}
		} catch (IOException e) {
			System.out.println("Error");
		}
	}

	public ProgrammerController() {

	}

	@FXML
	private void handleLogout(){
		try {
			this.main.getFileData().clear();
			this.main.getVersionData().clear();
			this.main.getPersonProjectsData().clear();
			this.main.getSearchProjectsData().clear();
			this.main.getServer().out.writeUTF("Logout");
			this.main.showLogin();
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

	@FXML
	private void refreshOpenProject(){
		try {
			this.main.getPersonProjectsData().clear();
			this.main.getServer().out.writeUTF("myProject");
			int size = this.main.getServer().in.readInt();
			String pj, detail;
			for(int i=0;i<size;i++){
				pj = this.main.getServer().in.readUTF();
				detail = this.main.getServer().in.readUTF();
				this.main.getPersonProjectsData().add(new ProjectModel(pj, detail, false));
			}
			pj = this.main.getServer().in.readUTF();
			//if(pj.equals("END"))
		} catch (IOException e) {
			System.out.println("error");
		}
	}

	@FXML
	private void handleRefreshJoinTable(){
		String s = projNameBar.getText();
		if(s==null)
			s="";
		this.refreshJoinProject(s);
	}

	private void refreshJoinProject(String newValue){
		try {
			this.main.getSearchProjectsData().clear();
			this.main.getServer().out.writeUTF("searchProject");
			String name = newValue;
			if(name==null)
				name = "";
			this.main.getServer().out.writeUTF(name);
			this.main.getServer().out.writeUTF(name);
			int size = this.main.getServer().in.readInt();
			String pj, detail;
			for(int i=0;i<size;i++){
				pj = this.main.getServer().in.readUTF();
				detail = this.main.getServer().in.readUTF();
				this.main.getSearchProjectsData().add(new ProjectModel(pj, detail, false));
			}
			pj = this.main.getServer().in.readUTF();
			//if(pj.equals("END"))
		} catch (IOException e) {
			System.out.println("error");
		}
	}

	@FXML
	private void initialize() {
		//this.refreshOpenProject();
		//this.refreshJoinProject("");

		openNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		openDetailsCol.setCellValueFactory(cellData -> cellData.getValue().detailProperty());

		joinNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		joinDetailsCol.setCellValueFactory(cellData -> cellData.getValue().detailProperty());

		openTable.getSelectionModel().selectedItemProperty().addListener(
				 (observable, oldValue, newValue) -> changelistener(newValue));
		joinTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> changelistener2(newValue));

		projNameBar.textProperty().addListener((observable, oldValue, newValue) -> refreshJoinProject(newValue));

		publicOrPrivate.selectedProperty().addListener((observable, oldValue, newValue) -> checkListener(newValue));

		projPassBar2.textProperty().addListener((observable, oldValue, newValue) -> checkMatch(newValue));
	}

	private void checkMatch(String newValue) {
		String pass = projPassBar.getText();
		String pass2 = newValue;
		if(pass.equals(pass2)){
			passMatchResult.setTextFill(Color.GREEN);
			projPassBar2.setStyle("-fx-border-color: green");
			passMatchResult.setText("Match!");
			}
		else{
			passMatchResult.setTextFill(Color.RED);
			projPassBar2.setStyle("-fx-border-color: red");
			passMatchResult.setText("Doesn't Match");
		}
	}

	private void checkListener(Boolean newValue) {
		projPassBar.setDisable(newValue.booleanValue());
		projPassBar2.setDisable(newValue.booleanValue());
	}

	private void changelistener(ProjectModel model){
		openButton.setDisable(false);
	}

	private void changelistener2(ProjectModel newValue) {
		if(!newValue.isPublic())
			PassBar.setDisable(false);
		else
			PassBar.setDisable(true);
		joinButton.setDisable(false);
	}

	public void joinToProject(){
		try {
			this.main.getServer().out.writeUTF("joinProject");
			String name = joinTable.getSelectionModel().getSelectedItem().getName();
			String pass;
			if(PassBar.isDisable())
				pass = "";
			else
				pass = PassBar.getText();
			this.main.getServer().out.writeUTF(name);
			this.main.getServer().out.writeUTF(pass);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("ERROR1")){
				joinResult.setText("Failed");
				System.out.println("Project not found");
			}
			else if(msg.equals("ERROR2")){
				joinResult.setText("Failed");
				System.out.println("pass is wrong");
			}
			else if(msg.equals("HASH_PROBLEM")){
				joinResult.setText("Failed");
				System.out.println("Server Failed");
			}
			else if(msg.equals("OK")){
				joinResult.setText("Joined");
			}
		} catch (IOException e) {
			System.out.println("Error");
		}
	}

	public void newProject(){
		try {
			String PN = newProjNameBar.getText();
			String PS, PS2;
			if(publicOrPrivate.isSelected()){
				PS = "";
				PS2 = "";
			}
			else{
				PS = projPassBar.getText();
				PS2 = projPassBar2.getText();
			}
			if(!PS.equals(PS2)){
				passMatchResult.setText("Passwords doesn't match");
				return ;
			}
			this.main.getServer().out.writeUTF("newProject");
			this.main.getServer().out.writeUTF(PN);
			this.main.getServer().out.writeUTF(PS);
			this.main.getServer().out.writeUTF(newProjDetail.getText());
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("CANT")){
				checkResult.setText("Projet name already exists");
			}
			else if(msg.equals("OK")){
				creatingResult.setText("Project created succsessfully");
			}
			else if(msg.equals("HASH_PROBLEM")){
				creatingResult.setText("Server couldn't respond");
			}
		}
		catch (IOException e) {
			System.out.println("ERROR");
		}
	}

	@FXML
	private void checkProjName(){
		try {
			this.main.getServer().out.writeUTF("checkProj");
			String name = newProjNameBar.getText();
			this.main.getServer().out.writeUTF(name);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("OK")){
				checkResult.setText("Valid");
				checkResult.setTextFill(Color.GREEN);
				newProjNameBar.setStyle("-fx-border-color: green");
			}
			else if(msg.equals("NOT_OK")){
				checkResult.setText("Already excist");
				checkResult.setTextFill(Color.RED);
				newProjNameBar.setStyle("-fx-border-color: red");
			}
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

}
