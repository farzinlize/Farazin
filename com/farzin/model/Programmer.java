package com.farzin.model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;

import com.farzin.PasswordHash;

@SuppressWarnings("serial")
public class Programmer implements Serializable{
	private String username;
	private String hashPassword;
	public String name;
	public boolean online;
	private ArrayList<Project> projects;
	private ArrayList<Activity> history;

	public Programmer(String u){
		this.username = u;
		this.projects = new ArrayList<Project>();
		this.history = new ArrayList<Activity>();
	}

	public Programmer(String u, String p){
			this.username = u;
			this.hashPassword = p;
			this.projects = new ArrayList<Project>();
			this.history = new ArrayList<Activity>();
	}

	public void securePass(){
		try {
			this.hashPassword = PasswordHash.createHash(hashPassword);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Project> getMyProj(){
		return projects;
	}

	public String getUsername(){
		return this.username;
	}

	public static ArrayList<Programmer> search(Programmer p, ArrayList<Programmer> DBP) throws NoSuchAlgorithmException, InvalidKeySpecException{
		ArrayList<Programmer> list = new ArrayList<Programmer>();
		Programmer user;
		for(int i=0;i<DBP.size();i++){
			user = (Programmer) DBP.get(i);
			if(p.username!=null && !user.username.equals(p.username))
				continue ;
			if(p.hashPassword!=null && !PasswordHash.validatePassword(p.hashPassword, user.hashPassword))
				continue ;
			if(p.name!=null && !user.name.equals(p.name))
				continue ;
			list.add(user);
		}
		return list;
	}

	public void joinProject(Project p){
		Activity a = new Activity(ActivityType.joinProject, this);
		a.date = new Date();
		this.history.add(a);
		this.projects.add(p);
	}

	public void newProjectCreat(Project p){
		Activity a = new Activity(ActivityType.newProject, this);
		a.date = new Date();
		this.history.add(a);
		this.projects.add(p);
	}

	public void loginCreat(){
		this.online = true;
		Activity a = new Activity(ActivityType.logIn, this);
		a.date = new Date();
		this.history.add(a);
	}

	public void logoutCreat(){
		this.online = false;
		Activity a = new Activity(ActivityType.logOut, this);
		a.date = new Date();
		this.history.add(a);
	}

	public void resetPass(String newPass) {
		Activity a = new Activity(ActivityType.resetPass, this);
		a.date = new Date();
		this.history.add(a);
		this.hashPassword = newPass;
		this.securePass();
	}

	public static Programmer newProgrammer(String username, String pass){
		Programmer p = new Programmer(username, pass);
		p.online = false;
		Activity a = new Activity(ActivityType.newUser, p);
		a.date = new Date();
		p.history.add(a);
		p.securePass();
		return p;
	}
/*
	public void run(Scanner scan) {
		//show GUI
		ArrayList<Object> list = new ArrayList<Object>();
		Activity a1 = new Activity(ActivityType.logIn, this);
		a1.date = new Date();
		this.history.add(a1);
		String command = "";
		while(true){
			command = scan.next();
			if(command.equals("logOut")){
				Activity a2 = new Activity(ActivityType.logOut, this);
				a2.date = new Date();
				this.history.add(a2);
				break ;
			}
			else if(command.equals("newProject")){
				Activity a2 = new Activity(ActivityType.newProject, this);
				a2.date = new Date();
				this.history.add(a2);
				Project pr = Project.newProject("ali", "ali", this);
				list.add(pr);
			}
			else if(command.equals("myProjects")){
				for(int i=0;i<this.projects.size();i++){
					System.out.println(this.projects.get(i).name);
				}
			}
			else
				System.out.println("invalid command");
			scan.reset();
		}
	}
*/
}
