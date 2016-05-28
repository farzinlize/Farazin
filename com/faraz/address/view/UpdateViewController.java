package com.faraz.address.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.faraz.address.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UpdateViewController {

	private MainApp main;
	private Stage stage;
	private String fileName;
	private String version;

	@FXML
	private TextField addressBar;

	public void setMain(MainApp mainApp, Stage dialog, String fileName, String ver) {
		this.main = mainApp;
		this.stage = dialog;
		this.fileName = fileName;
		this.version = ver;
	}

	private void update(){
		try {
			this.main.getServer().out.writeUTF("Update");
			this.main.getServer().out.writeUTF(fileName);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("FILE_NOT_FOUND")){
				System.out.println("File not found");
				return ;
			}
			int size = this.main.getServer().in.readInt();
			byte [] fileB = new byte [size];
			this.main.getServer().in.read(fileB);
			FileOutputStream fout = new FileOutputStream(new File(addressBar.getText() + fileName));
			fout.write(fileB);
			fout.close();
			this.stage.close();
		} catch (IOException e) {
			System.out.println("Error");
		}
	}

	@FXML
	private void handleDownload(){
		if(this.version==null)
			this.update();
		else
			this.downloadVersion();
	}

	private void downloadVersion() {
		try {
			this.main.getServer().out.writeUTF("DownloadVersion");
			this.main.getServer().out.writeUTF(fileName);
			this.main.getServer().out.writeUTF(version);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("FILE_NOT_FOUND")){
				System.out.println("file not found");
				return;
			}
			else if(msg.equals("VER_NOT_FOUND")){
				System.out.println("version not found");
				return;
			}
			int size = this.main.getServer().in.readInt();
			byte [] fileB = new byte [size];
			this.main.getServer().in.read(fileB);
			FileOutputStream fout = new FileOutputStream(new File(addressBar.getText() +version+ fileName));
			fout.write(fileB);
			fout.close();
			this.stage.close();
		} catch (IOException e) {
			System.out.println("Server Failed");
		}

	}

	@FXML
	private void handleBrowse(){
		FileChooser fileChooser = new FileChooser();
		File f = fileChooser.showSaveDialog(null);
		addressBar.setDisable(true);
		addressBar.setText(f.getAbsolutePath());
	}

	@FXML
	private void handleCancel(){
		this.stage.close();
	}

}
