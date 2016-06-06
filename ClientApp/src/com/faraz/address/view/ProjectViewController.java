package com.faraz.address.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.faraz.address.MainApp;
import com.faraz.address.model.HeadFileModel;
import com.faraz.address.model.VersioningModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class ProjectViewController {

	private	MainApp main;

	@FXML
	private Label checkNameValue;
	@FXML
	private Menu fileBar;
	@FXML
	private MenuItem newBar;
	@FXML
	private MenuItem deleteBar;
	@FXML
	private MenuItem closeBar;
	@FXML
	private Menu helpBar;
	@FXML
	private MenuItem aboutBar;
	@FXML
	private AnchorPane projPane;
	@FXML
	private TableView<HeadFileModel> filesTable;
	@FXML
	private TableColumn<HeadFileModel,String> fileName;
	@FXML
	private TableColumn<HeadFileModel,String> fileStatus;
	@FXML
	private TableColumn<HeadFileModel, String> fileDetails;
	@FXML
	private TableColumn<HeadFileModel, Integer> fileLastVersion;
	@FXML
	private TableView<VersioningModel> versionsTable;
	@FXML
	private TableColumn<VersioningModel, String> verName;
	@FXML
	private TableColumn<VersioningModel, String> verVer;
	@FXML
	private TableColumn<VersioningModel, String> verDetails;
	@FXML
	private TextField searchFile;
	@FXML
	private TextField searchVer;
	@FXML
	private Button fileRef;
	@FXML
	private Button versionRef;
	@FXML
	private SplitMenuButton profileBtn;
	@FXML
	private MenuItem editProject;
	@FXML
	private MenuItem profEditBtn;
	@FXML
	private MenuItem profExit;
	@FXML
	private MenuItem ProfLogout;
	@FXML
	private Label dirLbl;
	@FXML
	private Label statusLbl;
	@FXML
	private Label verLbl;
	@FXML
	private Label nameLbl;
	@FXML
	private Button history;
	@FXML
	private Button update;
	@FXML
	private Button lock;
	@FXML
	private Button unlock;
	@FXML
	private Button stealLock;
	@FXML
	private TextField fileAddress;
	@FXML
	private TextField commitName;
	@FXML
	private Button check;
	@FXML
	private Button commit;
	@FXML
	private Button chooseFile;
	@FXML
	private Button verDownload;
	private File fileForCommit;

	public void setMain(MainApp mainApp) {
		this.main = mainApp;

		filesTable.setItems(main.getFileData());
		versionsTable.setItems(main.getVersionData());
	}

	@FXML
	private void initialize() {
		//profileBtn.setText(getProgrammerNameFromServer());

		fileName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		fileStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		fileDetails.setCellValueFactory(cellData -> cellData.getValue().detailProperty());
		//fileLastVersion.setCellValueFactory(cellData -> cellData.getValue().lastVersionProperty().asObject());

		filesTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> fileTableListener(newValue));

		verName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		verVer.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		verDetails.setCellValueFactory(cellData -> cellData.getValue().makerProperty());

		searchFile.textProperty().addListener((observable, oldValue, newValue) -> refreshFileTable(newValue));
		searchVer.textProperty().addListener((observable, oldValue, newValue) -> refreshVerTable(newValue));
	}


	private void setFileLabels(HeadFileModel hfm){
		if(hfm!=null){
			nameLbl.setText(hfm.getName());
			dirLbl.setText(hfm.getDirectory());
			statusLbl.setText(hfm.getStatus());
			verLbl.setText(new Integer(hfm.getLastVersion()).toString());
		}
		else{
			nameLbl.setText("");
			dirLbl.setText("");
			statusLbl.setText("");
			verLbl.setText("");
		}
	}

	private void setVerTable(HeadFileModel hf){
		try {
			this.main.getVersionData().clear();
			this.main.getServer().out.writeUTF("showFileVersions");
			this.main.getServer().out.writeUTF(hf.getName());
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("FileNotFound")){
				System.out.println("file not found");
			}
			String name, date, maker;
			int size = this.main.getServer().in.readInt();
			for(int i=0;i<size;i++){
				name = this.main.getServer().in.readUTF();
				maker = this.main.getServer().in.readUTF();
				date = this.main.getServer().in.readUTF();
				this.main.getVersionData().add(new VersioningModel(name, date, maker));
			}
			msg = this.main.getServer().in.readUTF();
			//if msg ... END
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

	private void fileTableListener(HeadFileModel newValue) {
		setFileLabels(newValue);
		if(newValue!=null)
			setVerTable(newValue);
		if(newValue == null){
			lock.setDisable(true);
			unlock.setDisable(true);
			commit.setDisable(true);
			commitName.setDisable(true);
			chooseFile.setDisable(true);
			fileAddress.setDisable(true);
			check.setDisable(true);
			stealLock.setDisable(true);
		}
		else if((newValue.getStatus().equals("Lock") && newValue.getAm().equals("N")) ){
			lock.setDisable(true);
			unlock.setDisable(true);
			commit.setDisable(true);
			commitName.setDisable(true);
			chooseFile.setDisable(true);
			fileAddress.setDisable(true);
			check.setDisable(true);
			stealLock.setDisable(false);
		}
		else if(newValue.getStatus().equals("Lock") && newValue.getAm().equals("Y")){
			lock.setDisable(true);
			unlock.setDisable(false);
			commit.setDisable(false);
			commitName.setDisable(false);
			chooseFile.setDisable(false);
			fileAddress.setDisable(false);
			check.setDisable(false);
			stealLock.setDisable(true);
		}
		else{
			lock.setDisable(false);
			unlock.setDisable(true);
			commit.setDisable(true);
			commitName.setDisable(true);
			chooseFile.setDisable(true);
			fileAddress.setDisable(true);
			check.setDisable(true);
			stealLock.setDisable(true);
		}
	}

	private void refreshVerTable(String newValue) {
		// TODO Auto-generated method stub
	}

	@FXML
	private void handleRefreshFileTable(){
		//profileBtn.setText(getProgrammerNameFromServer());
		String name = searchFile.getText();
		if(name==null)
			name = "";
		refreshFileTable(name);
	}

	@FXML
	private void downloadVersion(){
		String name = filesTable.getSelectionModel().getSelectedItem().getName();
		String ver = versionsTable.getSelectionModel().getSelectedItem().getName();
		this.main.updateWindow(name, ver);
	}

	@FXML
	private void updateFile(){
		String name = filesTable.getSelectionModel().getSelectedItem().getName();
		this.main.updateWindow(name, null);
	}

	private void refreshFileTable(String newValue) {
		try {
			this.main.getFileData().clear();
			this.main.getServer().out.writeUTF("searchFile");
			String searchName;
			if(newValue==null)
				searchName = "";
			else
				searchName = newValue;
			this.main.getServer().out.writeUTF(searchName);
			this.main.getServer().out.writeUTF(searchName);
			String name, detail, status, am;
			int lastVersion;
			int size = this.main.getServer().in.readInt();
			//System.out.println("ProjectViewController|refreshTable|newValue="+newValue+"|size="+size);
			for(int i=0;i<size;i++){
				name = this.main.getServer().in.readUTF();
				detail = this.main.getServer().in.readUTF();
				lastVersion = this.main.getServer().in.readInt();
				status = this.main.getServer().in.readUTF();
				am = this.main.getServer().in.readUTF();
				this.main.getFileData().add(new HeadFileModel(name, detail, "/dir", lastVersion ,status, am));
			}
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("END")){

			}
				//System.out.println("Success");
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

	@FXML
	private void handleChooseForCommit(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a File to commit for : " + filesTable.getSelectionModel().getSelectedItem().getName());
		File f = fileChooser.showOpenDialog(null);
		fileAddress.setText(f.getAbsolutePath());
		fileAddress.setDisable(true);
		fileForCommit = f;
	}

	@FXML
	private void handleLock(){
		try {
			this.main.getServer().out.writeUTF("lock");
			String name = filesTable.getSelectionModel().getSelectedItem().getName();
			this.main.getServer().out.writeUTF(name);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("NOT_FOUND")){
				System.out.println("File not found");
			}
			else if(msg.equals("FILE_IS_LOCKED")){
				System.out.println("file is lock");
			}
			else if(msg.equals("SUCCSESS")){
				System.out.println("Ok!!!");
			}
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

	@FXML
	private void handleUnlock(){
		try {
			this.main.getServer().out.writeUTF("unlock");
			String name = filesTable.getSelectionModel().getSelectedItem().getName();
			this.main.getServer().out.writeUTF(name);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("NOT_FOUND")){
				System.out.println("File not found");
			}
			else if(msg.equals("ERROR")){
				System.out.println("file isn't lock");
			}
			else if(msg.equals("ACCESS_DENIED")){
				System.out.println("file is in another man lock");
			}
			else if(msg.equals("SUCCESS")){
				System.out.println("its good");
			}
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

	@FXML
	private void handleStealLock(){
		try {
			this.main.getServer().out.writeUTF("StealLock");
			String name = filesTable.getSelectionModel().getSelectedItem().getName();
			this.main.getServer().out.writeUTF(name);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("NOT_FOUND")){
				System.out.println("not found");
			}
			else if(msg.equals("SUCCESS")){
				System.out.println("good job");
			}
		} catch (IOException e) {
			System.out.println("Server down");
		}
	}

	@FXML
	private void handleCommit(){
		try {
			this.main.getServer().out.writeUTF("commit");
			String name = filesTable.getSelectionModel().getSelectedItem().getName();
			String newName = commitName.getText();
			this.main.getServer().out.writeUTF(name);
			this.main.getServer().out.writeUTF(newName);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("NOT_FOUND")){
				System.out.println("not found");
			}
			else if(msg.equals("ERROR")){
				System.out.println("File not commitable");
			}
			else if(msg.equals("ACCESS_DENY")){
				System.out.println("access denied");
			}
			else if(msg.equals("OK")){
				File f;
				if(fileForCommit!=null){
					f = fileForCommit;
				}
				else{
					String address = fileAddress.getText();
					f = new File(address);
					//String name2 = commitName.getText();
				}
				FileInputStream fin = new FileInputStream(f);
				byte [] fileB = new byte [(int) f.length()];
				fin.read(fileB);
				fin.close();
				this.main.getServer().out.writeInt((int) f.length());
				this.main.getServer().out.write(fileB);
			}
		} catch (IOException e) {
			System.out.println("Server Down");
		}
	}

	@FXML
	private void handleLogout(){
		try {
			this.main.getFileData().clear();
			this.main.getVersionData().clear();
			this.main.getPersonProjectsData().clear();
			this.main.getSearchProjectsData().clear();
			this.main.getServer().out.writeUTF("q_project");
			this.main.getServer().out.writeUTF("Logout");
			this.main.showLogin();
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

	@FXML
	private void exitProject(){
		try {
			this.main.getFileData().clear();
			this.main.getVersionData().clear();
			this.main.getServer().out.writeUTF("q_project");
			this.main.ProgrammerView();
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

	@FXML
	private void makeNewFile(){
		this.main.newFileWindow();
	}

	@FXML
	private void handleHistory(){
		String name = filesTable.getSelectionModel().getSelectedItem().getName();
		this.main.fileHistoryWindow(name);
	}

	@SuppressWarnings("unused")
	private String getProgrammerNameFromServer(){
		try {
			this.main.getServer().out.writeUTF("ProgrammerName");
			String name;
			name = this.main.getServer().in.readUTF();
			return name;
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
		return null;
	}

}
