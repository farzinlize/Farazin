package com.farzin.main;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable {

	private Main main;
	private ServerSocket listener;
	private int PORT;
	public boolean serverOn;

	public void serverLaunch(){
		serverOn = true;
		try {
			while(serverOn){
				this.main.controller.toOut("waiting for client...");
				new Thread(new client(listener.accept(), this.main)).start();
				this.main.controller.toOut("client accepted");
			}

		} catch (IOException e) {
			this.main.controller.toOut("(not recieved)");
		}
	}

	public Server (Main main, int port){
		this.main = main;
		this.PORT = port;
	}

	public void setMain(Main main){
		this.main = main;
	}

	public Main getMain(){
		return main;
	}

	@Override
	public void run() {
		try {
			this.serverOn = false;
			//this.PORT = 14565;
			this.main.controller.toOut("Server is running...");
			this.listener =  new ServerSocket(this.PORT);
			this.main.controller.toOut("Port is Open");
			serverLaunch();
		} catch (IOException e) {
			this.main.controller.toOut("PORT is closed");
		}
	}

}
