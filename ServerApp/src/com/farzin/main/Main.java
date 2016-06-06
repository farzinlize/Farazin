package com.farzin.main;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

import com.farzin.PasswordHash;
import com.farzin.view.ServerViewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{

	private SynchronousQueue<String> responds;
	private DataBase DB;
	public Thread serverSide;
	public Server server;
	public ServerViewController controller;
	//private ServerSocket listener;
	//private int PORT;
//	public boolean serverOn;
	private Stage primaryStage;

	public DataBase getData(){
		return this.DB;
	}

	public String getRespondes(){
		try {
			return responds.take();
		} catch (InterruptedException e) {
			System.out.println("FUCK!  2  ");
			return null;
		}
	}

	public void putRespondes(String st){
		try {
			responds.put(st);
		} catch (InterruptedException e) {
			System.out.println("FUCK!  1  ");
			//e.printStackTrace();
		}
	}

	public SynchronousQueue<String> getQue(){
		return responds;
	}

//	public int getPort(){
//		return PORT;
//	}

//	public void Maincreat(int port){
//		try {
//			this.serverOn = false;
//			this.PORT = port;
//			System.out.println("Server is running...");
//			this.listener =  new ServerSocket(port);
//			System.out.println("Port is Open");
//			this.DB = new DataBase();
//		} catch (IOException e) {
//			System.out.println("PORT is closed");
//		}
//	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = arg0;
		this.primaryStage.setTitle("Farazin");

		this.DB = new DataBase();

		show();
		server = new Server(this, 14565);
		this.serverSide = new Thread(server);
		serverSide.start();
		//serverSide.wait();
		//serverSide.run();
		System.out.println("Hi --> " + serverSide.getName());

	}

	public void show(){
	try {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PasswordHash.class.getResource("view/ServerView.fxml"));
		AnchorPane loginPage = (AnchorPane) loader.load();

		Scene scene = new Scene(loginPage);
		primaryStage.setScene(scene);
		primaryStage.show();

		this.controller = loader.getController();
		this.controller.setMain(this);


	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("FXML load Failed");
		}
	}

	public static void main(String [] args){
		launch(args);
		//Main m = new Main(14565);
		//Thread ui = new Thread(new UI(m));
		//ui.start();
		//m.serverLaunch();
	}

}