package com.farzin.model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;

import com.farzin.PasswordHash;

@SuppressWarnings("serial")
public class Project implements Serializable{
	public String name;
	private String Pass;
	private String details;
	private ArrayList<HeadFile> Files;
	private ArrayList<Activity> history;

	public Project(String name, String pass, String details) throws NoSuchAlgorithmException, InvalidKeySpecException{
		this.name = name;
		this.details = details;
		if(pass==null || pass.equals(""))
			this.Pass = "";
		else
			this.Pass = PasswordHash.createHash(pass);
		this.Files = new ArrayList<HeadFile>();
		this.history = new ArrayList<Activity>();
	}

	public static Project newProject(String name, String pass, String details, Programmer maker) throws NoSuchAlgorithmException, InvalidKeySpecException{
		Project p = new Project(name, pass, details);
		Activity a = new Activity(ActivityType.newProject, maker);
		a.date = new Date();
		p.history.add(a);
		return p;
	}

	public String getDetail(){
		return details;
	}

	public String getPass(){
		return Pass;
	}

	public void addFile(HeadFile hf){
		this.Files.add(hf);
	}

	public ArrayList<HeadFile> getFiles(){
		return Files;
	}

	public static ArrayList<Project> search(String name, ArrayList<Project> DBP){
		ArrayList<Project> list = new ArrayList<Project>();
		Project pj = null;
		for(int i=0;i<DBP.size();i++){
			pj = DBP.get(i);
			if(pj.name.equals(name))
				list.add(pj);
		}
		return list;
	}

	public static ArrayList<Project> searchOnTheGo(String halfName, String detail,ArrayList<Project> DBP){
		ArrayList<Project> list = new ArrayList<Project>();
		Project pj = null;
		for(int i=0;i<DBP.size();i++){
			pj = DBP.get(i);
			if(pj.name.contains(halfName)){
				list.add(pj);
				continue ;
			}
			if(pj.details.contains(detail)){
				list.add(pj);
				continue ;
			}
		}
		return list;
	}

}
