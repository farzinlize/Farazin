package com.faraz.address.view;

import java.io.IOException;

import com.faraz.address.MainApp;
import com.faraz.address.model.ActivityModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FileHistoryViewController {

	private MainApp main;
	private Stage stage;
	private String headFileName;

	@FXML
	private Label fileName;
	@FXML
	private Label fileType;
	@FXML
	private Label Activity;
	@FXML
	private Label Date;
	@FXML
	private Label user;
	@FXML
	private Button close;
	@FXML
	private Button refresh;
	@FXML
	private TextField search;
	@FXML
	private TableView<ActivityModel> historyTable;
	@FXML
	private TableColumn<ActivityModel, String> typeColumn;
	@FXML
	private TableColumn<ActivityModel, String> dataColumn;

	public void setMain(MainApp mainApp, Stage dialog, String name) {
		this.main = mainApp;
		this.stage = dialog;
		this.headFileName = name;

		historyTable.setItems(mainApp.getFileHistory());
	}

	@FXML
	private void initialize() {
		setLabels(null);

		typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		dataColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

		historyTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> historyTableListener(newValue));
	}

	private String onlyName(){
		int i=0;
		for(i=0;i<headFileName.length();i++)
			if(headFileName.charAt(i)=='.')
				break ;
		return headFileName.substring(0, i);
	}

	private String onlyFileType(){
		int i=0;
		for(i=0;i<headFileName.length();i++)
			if(headFileName.charAt(i)=='.')
				break ;
		return headFileName.substring(i, headFileName.length());
	}

	private void setLabels(ActivityModel newValue){
		if(newValue!=null){
			Activity.setText(newValue.getType());
			Date.setText(newValue.getDate());
			user.setText(newValue.getProg());
		}
		else{
			Activity.setText("");
			Date.setText("");
			user.setText("");
		}
	}

	private void historyTableListener(ActivityModel newValue) {
		setLabels(newValue);
	}

	@FXML
	private void handleClose(){
		this.stage.close();
	}

	@FXML
	private void handleRefresh(){
		System.out.println(": /");
		try {
			fileName.setText(this.onlyName());
			fileType.setText(this.onlyFileType());
			this.main.getServer().out.writeUTF("FileHistory");
			this.main.getFileHistory().clear();
			this.main.getServer().out.writeUTF(headFileName);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("FILE_NOT_FOUND")){
				System.out.println("File not found");
				return ;
			}
			String date, maker, type;
			int size = this.main.getServer().in.readInt();
			for(int i=0;i<size;i++){
				date = this.main.getServer().in.readUTF();
				maker = this.main.getServer().in.readUTF();
				type = this.main.getServer().in.readUTF();
				this.main.getFileHistory().add(new ActivityModel(type, maker, date));
			}
			msg = this.main.getServer().in.readUTF();
			//if msg ... END
		} catch (IOException e) {
			System.out.println("Server Failed");
		}
	}

}
