package com.faraz.address.view;

import java.io.IOException;

import com.faraz.address.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LoginController {

	@FXML
	private Label passMatch;
	@FXML
	private Label userCheck;
	@FXML
	private Label createResult;
	@FXML
	private TabPane tabs;
	@FXML
	private Tab signIn;
	@FXML
	private Tab signUp;
	@FXML
	private Button login;
	@FXML
	private Button creatNew;
	@FXML
	private Button cheakUser;
	@FXML
	private TextField userLogin;
	@FXML
	private PasswordField passLogin;
	@FXML
	private TextField userNew;
	@FXML
	private PasswordField passNew;
	@FXML
	private PasswordField passAgainNew;
	@FXML
	private TextField nameNew;
	@FXML
	private Label loginLabel;
	@FXML
	private Label newLabel;
	@FXML
	private Hyperlink forgot;

	private MainApp main;

	public void setMain(MainApp main){
		this.main = main;
	}

	@FXML
    private void initialize() {
		passAgainNew.textProperty().addListener((observable, oldValue, newValue) -> checkMatch(newValue));
	}

	public void checkMatch(String newValue){
		String pass = passNew.getText();
		String pass2 = newValue;
		if(pass.equals(pass2)){
			passMatch.setTextFill(Color.GREEN);
			passAgainNew.setStyle("-fx-border-color: green");
			passMatch.setText("Match!");
			}
		else{
			passMatch.setTextFill(Color.RED);
			passAgainNew.setStyle("-fx-border-color: red");
			passMatch.setText("Doesn't Match");
		}
	}

	@FXML
	public void newUser(ActionEvent event){
		try{
			this.main.getServer().out.writeUTF("New");
			String UN = userNew.getText();
			String PS = passNew.getText();
			String PS2 = passAgainNew.getText();
			String NM = nameNew.getText();
			this.main.getServer().out.writeUTF(UN);
			this.main.getServer().out.writeUTF(PS);
			this.main.getServer().out.writeUTF(PS2);
			this.main.getServer().out.writeUTF(NM);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("ERROR1")){
				System.out.println("Username already exist");
			}
			else if(msg.equals("ERROR2")){
				System.out.println("pass not match");
			}
			else if(msg.equals("OK"))
				this.createResult.setText("Success");
			else if(msg.equals("HASH_PROBLEM"))
				this.createResult.setText("Server Failed");
		} catch (IOException e){
			System.out.println("ERROR");
		}
	}

	@FXML
	public void loginButton(ActionEvent event){
		try {
			this.main.getServer().out.writeUTF("Login");
			String UN = userLogin.getText();
			String PS = passLogin.getText();
			this.main.getServer().out.writeUTF(UN);
			this.main.getServer().out.writeUTF(PS);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("notAccepted")){
				this.loginLabel.setText("Username or password incorect");
			}
			else if(msg.equals("ERROR")){
				this.loginLabel.setText("User already online");
			}
			else if(msg.equals("Accepted")){
				this.loginLabel.setText("Welcome");
				this.main.ProgrammerView();
			}
			else if(msg.equals("HASH_PROBLEM")){
				this.loginLabel.setText("Server Failed");
			}
		} catch (IOException e) {
			System.out.println("ERROR");
		}

	}

	@FXML
	public void resetPass(){

	}

	public void checkUsername(){
		try {
			this.main.getServer().out.writeUTF("Check");
			String UN = userNew.getText();
			this.main.getServer().out.writeUTF(UN);
			String msg = this.main.getServer().in.readUTF();
			if(msg.equals("OK")){
				userCheck.setText("Valid");
				userCheck.setTextFill(Color.GREEN);
				userNew.setStyle("-fx-border-color: green");
			}
			else if(msg.equals("NOT_OK")){
				userCheck.setText("Already exsists");
				userCheck.setTextFill(Color.RED);
				userNew.setStyle("-fx-border-color: red");
			}
			else if(msg.equals("HASH_PROBLEM")){
				createResult.setText("Server Failed");
			}
		} catch (IOException e) {
			System.out.println("ERROR");
		}
	}
}
