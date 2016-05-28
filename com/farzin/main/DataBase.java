package com.farzin.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.farzin.model.Programmer;
import com.farzin.model.Project;
import com.farzin.view.ServerViewController;

@SuppressWarnings("serial")
public class DataBase implements Serializable{

	private ArrayList<Programmer> allProgrammers;
	private ArrayList<Project> allProjects;

	public static DataBase DeSerialize(){
		DataBase db = null;
		try{
			System.out.println("Attempting to DeSerialaze DataBase ...");
			FileInputStream filein = new FileInputStream("C:\\Users\\Farzin\\Desktop\\FUCK2.ser");
			ObjectInputStream in = new ObjectInputStream(filein);
			db = (DataBase) in.readObject();
			in.close();
			filein.close();
			System.out.println("DeSerialize sucessful");
		} catch (IOException e){
			System.out.println("I/O Exeption in DeSerialize");
		} catch (ClassNotFoundException e) {
			System.out.println("classNotFound in DeSerialize");
		}
		return db;
	}

	public void Serialize(ServerViewController controller){
		try {
			System.out.println("Attempting to serialize DataBase...");
			FileOutputStream fileout = new FileOutputStream(new File("C:\\Users\\Farzin\\Desktop\\FUCK2.ser"));
			ObjectOutputStream out = new ObjectOutputStream(fileout);
			out.writeObject(this);
			out.close();
			fileout.close();
			System.out.println("Serialize successful");

		} catch (FileNotFoundException e) {
			System.out.println("FileNotFound Exeption");
		} catch (IOException e) {
			System.out.println("I/O Exeption");
		}
	}

	public DataBase() {
		this.allProgrammers = new ArrayList<Programmer>();
		this.allProjects = new ArrayList<Project>();
	}

	public ArrayList<Programmer> getAllProg(){
		return this.allProgrammers;
	}

	public ArrayList<Project> getAllProj(){
		return this.allProjects;
	}

	public void addPro(Programmer p){
		this.allProgrammers.add(p);
	}

	public void addProj(Project pj){
		this.allProjects.add(pj);
	}

}
