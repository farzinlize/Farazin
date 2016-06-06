package com.faraz.address;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
//import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
//import java.util.Scanner;

public class Server implements Runnable {

	public DataInputStream in;
	public DataOutputStream out;
	private Socket s;
//	private PrintWriter pw;
//	private boolean log;

	public Server(){
		try {
			System.out.println("Connecting to Server localhost on port 14565");
			this.s = new Socket("localhost", 14565);
			System.out.println("Socket conected");
			this.in = new DataInputStream(this.s.getInputStream());
			this.out = new DataOutputStream(this.s.getOutputStream());
//			this.pw = new PrintWriter(this.s.getOutputStream());
//			this.log=false;

		} catch (UnknownHostException e) {
			System.out.println("Server Faild (1)");
		} catch (IOException e) {
			System.out.println("Server Faild (2)");
		}
	}

	@Override
	public void run() {

	}

}

