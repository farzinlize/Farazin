package com.farzin.view;

import com.farzin.main.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ServerViewController {

	@FXML
	private TextArea console;
	@FXML
	private Button startOff;

	private Main mainApp;

	@FXML
	public void change(ActionEvent event){
		this.mainApp.controller.toOut("Start Testing ...");
		if(this.mainApp.server.serverOn){
			this.mainApp.server.serverOn = false;
		}
		else{
			this.mainApp.server.serverOn = true;
		}
	}

	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
	}

	public void toOut(String st){
		st = st.concat("\n");
		String befor = console.getText();
		console.setText(befor.concat(st));
	}

}
