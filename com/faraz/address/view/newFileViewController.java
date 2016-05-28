package com.faraz.address.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.faraz.address.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class newFileViewController {

	private MainApp main;
	private Stage stage;

	@FXML
	private Button cancel;
	@FXML
	private TextField fileAddress;
	@FXML
	private TextField fileName;
	@FXML
	private TextField fileDirectory;
	@FXML
	private TextArea fileDetail;
	@FXML
	private Label resultShow;
	@FXML
	private Label name;
	@FXML
	private Label type;

	private File fileNew;

	public void setMain(MainApp mainApp, Stage stage) {
		this.main = mainApp;

		this.stage = stage;
	}

	@FXML
	private void handleChooseForCommit(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a File ...");
		File f = fileChooser.showOpenDialog(null);
		fileAddress.setText(f.getAbsolutePath());
		fileAddress.setDisable(true);
		name.setText(this.onlyName(f.getName()));
		type.setText(this.onlyFileType(f.getName()));
		fileNew = f;
	}

	private String onlyName(String name){
		int i=0;
		for(i=0;i<name.length();i++)
			if(name.charAt(i)=='.')
				break ;
		return name.substring(0, i);
	}

	private String onlyFileType(String name){
		int i=0;
		for(i=0;i<name.length();i++)
			if(name.charAt(i)=='.')
				break ;
		return name.substring(i, name.length());
	}

	@FXML
	private void createIt(){
		try {
			this.main.getServer().out.writeUTF("newFile");
			String name = fileName.getText();
			String detail = fileDetail.getText();
			this.main.getServer().out.writeUTF(name);
			this.main.getServer().out.writeUTF(detail);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("FILE_NAME_ERROR")){
				resultShow.setText("File Already exist");
				return ;
			}
			File f;
			if(fileNew!=null)
				f = fileNew;
			else
				f = new File(fileAddress.getText());
			FileInputStream fin = new FileInputStream(f);
			int size = (int) f.length();
			byte [] fileB = new byte [size];
			fin.read(fileB);
			fin.close();
			this.main.getServer().out.writeInt(size);
			this.main.getServer().out.write(fileB);
			msg = this.main.getServer().in.readUTF();
			if(msg.equals("SUCCESS")){
				resultShow.setText("File Created");
			}
			this.stage.close();
		} catch (IOException e) {
			System.out.println("Server Error");
		}
	}

	@FXML
	private void cancelThis(){
		this.stage.close();
	}

}
