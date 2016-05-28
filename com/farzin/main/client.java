package com.farzin.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import com.farzin.PasswordHash;
import com.farzin.model.Activity;
import com.farzin.model.FileStatus;
import com.farzin.model.HeadFile;
import com.farzin.model.Programmer;
import com.farzin.model.Project;
import com.farzin.model.Versioning;

public class client implements Runnable {

	private DataInputStream input;
	private DataOutputStream output;
	public Socket socket;					//private
	public int clientNum;
	private Main mainApp;

	private static int count = 0;

	public client(Socket s, Main main) {
		try {
			this.mainApp = main;
			this.input = new DataInputStream(s.getInputStream());
			this.output = new DataOutputStream(s.getOutputStream());
			this.socket = s;
			this.clientNum = count++;
			this.mainApp.controller.toOut("HI");
			System.out.println("Client Connected with client Numner :" + clientNum);
		} catch (IOException e) {
			System.out.println("Client couldn't connect | client Num :" + clientNum);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String command = "";
			while(true){
				command = this.input.readUTF();
				System.out.println("Client Number " + clientNum + " Enter a command : " + command);
				if(command.equals("exit")){
					this.exit();
					break ;
				}
				else if(command.equals("Check"))
					this.checkUsername();
				else if(command.equals("ResetPass"))
					this.resetPass();
				else if(command.equals("New"))
					this.newUser();
				else if(command.equals("Login"))
					this.login();
				else{
					System.out.println("Eror");
					break ;
				}
			}
		} catch (IOException e){
			System.out.println("can't read | client Number : " + clientNum);
		}
		System.out.println("Client number " + clientNum + " closed");
	}

	private void checkUsername() throws IOException {
		System.out.println("cheack for a Username ... | client Number : " + clientNum);
		String UN = this.input.readUTF();
		ArrayList<Programmer> list;
		try {
			list = Programmer.search(new Programmer(UN), this.mainApp.getData().getAllProg());
			if(list == null || list.size()==0){
				this.output.writeUTF("OK");
				System.out.println("Username not found and its good | client Number : " + clientNum);
				}
			else{
				this.output.writeUTF("NOT_OK");
				System.out.println("Username already exist | client Number : " + clientNum);
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("Hash Problem :(");
			this.output.writeUTF("HASH_PROBLEM");
		}
	}

	private void resetPass() throws IOException {
		System.out.println("Reseting a Password ... | client Number : " + clientNum);
		String UN = this.input.readUTF();
		String SC = this.input.readUTF();
		ArrayList<Programmer> list;
		try {
			list = Programmer.search(new Programmer(UN), this.mainApp.getData().getAllProg());
			if(list == null || list.size()==0){
				this.output.writeUTF("ERROR1");
				System.out.println("Reseting Password Failed (Username not found) | client Number : " + clientNum);
				return ;
			}
			Programmer p = list.get(0);
			if(!p.name.equals(SC)){
				this.output.writeUTF("ERROR2");
				System.out.println("Reseting Password Failed (seciurity code incorect) | client Number : " + clientNum);
				return ;
			}
			this.output.writeUTF("OK");
			String NP = this.input.readUTF();
			p.resetPass(NP);
			System.out.println("Reset Password successful");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("Hash Problem :(");
			this.output.writeUTF("HASH_PROBLEM");
		}
	}

	private void newUser() throws IOException {
		System.out.println("New User Making ... | client Number : " + clientNum);
		String UN = this.input.readUTF();
		String PS = this.input.readUTF();
		String PS2 = this.input.readUTF();
		String NM = this.input.readUTF();
		ArrayList<Programmer> list;
		try {
			list = Programmer.search(new Programmer(UN), this.mainApp.getData().getAllProg());
			if(list.size()!=0){
				System.out.println("Username already exists | client Number : " + clientNum);
				this.output.writeUTF("ERROR1");
				return ;
			}
			if(!PS.equals(PS2)){
				System.out.println("Error while Making a user | client Number : " + clientNum);
				this.output.writeUTF("ERROR2");
				return ;
			}
			Programmer p = Programmer.newProgrammer(UN, PS);
			p.name = NM;
			this.mainApp.getData().addPro(p);
			System.out.println("User Created with name of "+p.name+" | client Number : "+clientNum);
			this.output.writeUTF("OK");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("Hash Problem :(");
			this.output.writeUTF("HASH_PROBLEM");
		}
	}

	private void login() throws IOException {
		System.out.println("Login in progress ... | client Number : " + clientNum);
		String UN = this.input.readUTF();
		String PS = this.input.readUTF();
		ArrayList<Programmer> list;
		try {
			list = Programmer.search(new Programmer(UN, PS), this.mainApp.getData().getAllProg());
			if(list == null || list.size()==0){
				System.out.println("username or password incorect | client Number : " + clientNum);
				this.output.writeUTF("notAccepted");
				return ;
			}
			Programmer p = list.get(0);
			if(p.online){
				System.out.println("User " + p.name + " already online | client Number : " + clientNum);
				this.output.writeUTF("ERROR");
				return ;
			}
			p.loginCreat();
			System.out.println("Login successful | Programmer : " + p.name + " | client Number : " + clientNum);
			this.output.writeUTF("Accepted");
			this.programmerView(p);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("Hash Problem :(");
			this.output.writeUTF("HASH_PROBLEM");
		}
	}

	private void programmerView(Programmer p) {
		try {
			String command = "";
			while(true){
				command = this.input.readUTF();
				System.out.println("Client Number " + clientNum + " by name of : " + p.name + " Enter a command : " + command);
				if(command.equals("Logout")){
					p.logoutCreat();
					System.out.println("user : " + p.name + " logout | client Number : " + clientNum);
					return ;
					}
				else if(command.equals("joinProject")){
					this.joinToProject(p);
					}
				else if(command.equals("checkProj")){
					this.checkProjName(p);
					}
				else if(command.equals("searchProject"))
					this.serachForProj(p);
				else if(command.equals("openProject")){
					this.openProject(p);
					}
				else if(command.equals("newProject")){
					this.newProj(p);
					}
				else if(command.equals("myProject")){
					this.showMyProj(p);
					}
				else if(command.equals("END_SERVER")){
					if(p.getUsername().equals("s9332041")){
						//this.mainApp.serverOn = false;
						return ;
						}
					}
				else{
					System.out.println("ERROR");
					return ;
					}
				}
			} catch (IOException e) {
				System.out.println("can't read | Programmer : "+ p.name+ " client Number : " + clientNum);
				p.logoutCreat();
				}
	}

	private void joinToProject(Programmer p) throws IOException {
		System.out.println("join to a project running by " + p.name +  " | client Number : " + clientNum);
		String name = this.input.readUTF();
		String pass = this.input.readUTF();
		ArrayList<Project> list = Project.search(name , this.mainApp.getData().getAllProj());
		if(list == null || list.size()==0){
			this.output.writeUTF("ERROR1");
			System.out.println("Project not found | Programmer name : " + p.name + " client Number : " + clientNum);
			return ;
			}
		Project pj = list.get(0);
		try {
			if(pass.equals(pj.getPass())){
				System.out.println("Programmer " + p.name + " joined " + pj.name + " Project | client Number : " + clientNum);
				this.output.writeUTF("OK");
				p.joinProject(pj);
			}
			else if(PasswordHash.validatePassword(pass, pj.getPass())){
				System.out.println("Programmer " + p.name + " joined " + pj.name + " Project | client Number : " + clientNum);
				this.output.writeUTF("OK");
				p.joinProject(pj);
			}
			else{
				System.out.println("pass is wrong | programmer : " + p.name + " client number : " + clientNum);
				this.output.writeUTF("ERROR2");
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("hash problem :(");
			this.output.writeUTF("HASH_PROBLEM");
		}
	}

	private void checkProjName(Programmer p) throws IOException {
		System.out.println("check for a Project by " + p.name + " ... | client Number : " + clientNum);
		String name = this.input.readUTF();
		ArrayList<Project> list;
		list = Project.search(name , this.mainApp.getData().getAllProj());
		if(list == null || list.size()==0){
			this.output.writeUTF("OK");
			System.out.println("Project not found and its good | Programmer name : " + p.name + " client Number : " + clientNum);
			}
		else{
			this.output.writeUTF("NOT_OK");
			System.out.println("Project with this name already exsist | Programmer name : " + p.name + " client Number : " + clientNum);
		}
	}

	private void serachForProj(Programmer p) throws IOException {
		System.out.println("Project Search start for " + p.name + " | client number : " + clientNum);
		String name = this.input.readUTF();
		String detail = this.input.readUTF();
		Project pj = null;
		ArrayList<Project> list = Project.searchOnTheGo(name, detail, this.mainApp.getData().getAllProj());
		this.output.writeInt(list.size());
		for(int i=0;i<list.size();i++){
			pj = list.get(i);
			this.output.writeUTF(pj.name);
			this.output.writeUTF(pj.getDetail());
		}
		this.output.writeUTF("END");
	}

	private void openProject(Programmer p) throws IOException {
		String name = this.input.readUTF();
		ArrayList<Project> list = Project.search(name, p.getMyProj());
		if(list == null || list.size()==0){
			System.out.println("Project not found | client Number : " + clientNum + " by name of : " + p.name);
			this.output.writeUTF("ERROR");
			return ;
		}
		Project pj = list.get(0);
		System.out.println("Project " + pj.name + " opened by User " + p.name + " | client Number : " + clientNum);
		this.output.writeUTF("OK");
		this.ProjectView(p, pj);
	}

	private void ProjectView(Programmer p, Project pj) throws IOException {
		String command = "";
		while(true){
			command = this.input.readUTF();
			System.out.println("Client Number " + clientNum + " by name of " + p.name + " in " + pj.name + "Project Enter a command : " + command);
			if(command.equals("q_project")){
				System.out.println("User " + p.name + " Exit frome " + pj.name + " Project | client Number : " + clientNum);
				return ;
			}
			else if(command.equals("FileHistory")){
				this.fileHistory(p, pj);
			}
			else if(command.equals("ProgrammerName")){
				this.sendProgrammerName(p);
			}
			else if(command.equals("searchFile")){
				this.searchForFile(p, pj);
			}
			else if(command.equals("StealLock")){
				this.stealLock(p, pj);
			}
			else if(command.equals("commit")){
				this.commitFile(p, pj);
			}
			else if(command.equals("lock")){
				this.lockFile(p, pj);
			}
			else if(command.equals("amITheLocker")){
				this.findLocker(p, pj);
			}
			else if(command.equals("unlock")){
				this.unLockFile(p, pj);
			}
			else if(command.equals("showFiles")){
				this.showFile(pj);
			}
			else if(command.equals("showFileVersions")){
				this.showFileVersions(p ,pj);
			}
			else if(command.equals("newFile")){
				this.newFile(p, pj);
			}
			else if(command.equals("Update")){
				this.updateFile(p, pj);
			}
			else if(command.equals("DownloadVersion")){
				this.downloadVersionFile(p, pj);
			}
			else{
				System.out.println("ERROR | client Number : " + clientNum);
			}
		}
	}

	private void fileHistory(Programmer p, Project pj) throws IOException {
		// TODO Auto-generated method stub
		String name = this.input.readUTF();
		ArrayList<HeadFile> list = HeadFile.search(name, pj.getFiles());
		if(list == null || list.size()==0){
			System.out.println("File not Found in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("FILE_NOT_FOUND");
			return ;
		}
		this.output.writeUTF("OK");
		HeadFile hf = list.get(0);
		ArrayList<Activity> list2 = hf.getHistory();
		Activity a;
		this.output.writeInt(list2.size());
		for(int i=0;i<list2.size();i++){
			a = list2.get(i);
			this.output.writeUTF(a.date.toString());
			this.output.writeUTF(a.getMaker().name);
			this.output.writeUTF(a.type.toString());
		}
		this.output.writeUTF("END");
	}

	private void downloadVersionFile(Programmer p, Project pj) throws IOException {
		String name = this.input.readUTF();
		String ver = this.input.readUTF();
		ArrayList<HeadFile> list = HeadFile.search(name, pj.getFiles());
		if(list == null || list.size()==0){
			System.out.println("File not Found in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("FILE_NOT_FOUND");
			return ;
		}
		HeadFile hf = list.get(0);
		File f = hf.downloadVersion(p, ver);
		if(f==null){
			System.out.println("Version not Found from File "+ hf.Name +" in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("VER_NOT_FOUND");
			return ;
		}
		this.output.writeUTF("OK");
		FileInputStream fIn = new FileInputStream(f);
		byte [] fileB = new byte [(int) f.length()];
		fIn.read(fileB);
		this.output.writeInt((int) f.length());
		this.output.write(fileB);
		fIn.close();
		System.out.println("File "+hf.Name+" Version "+ver +" downloaded for "+p.name+" in "+pj.name+" Project | client Number : "+clientNum);
		this.output.writeUTF("SUCCESS");
	}

	private void findLocker(Programmer p, Project pj) {
		// TODO Auto-generated method stub

	}

	private void showFileVersions(Programmer p, Project pj) throws IOException {
		System.out.println("Attempting to show old versions of a File in "+pj.name+" | Client number : "+clientNum+" Programmer : "+p.name);
		String fileName = this.input.readUTF();
		ArrayList<HeadFile> list1 = HeadFile.search(fileName, pj.getFiles());
		if(list1==null || list1.size()==0){
			System.out.println("File not found in "+pj.name+" | client number : "+clientNum+" Programmer : "+p.name);
			this.output.writeUTF("FileNotFound");
			return ;
		}
		this.output.writeUTF("OK");
		HeadFile hf = list1.get(0);
		ArrayList<Versioning> list2 = hf.fileVersions();
		Versioning v;
		this.output.writeInt(list2.size());
		for(int i=0;i<list2.size();i++){
			v = list2.get(i);
			this.output.writeUTF(v.Name);
			this.output.writeUTF(v.getPrePro().name);
			this.output.writeUTF(v.date.toString());
		}
		this.output.writeUTF("END");
	}

	private void sendProgrammerName(Programmer p) throws IOException {
		System.out.println("Sending Programmer name to client number "+clientNum);
		this.output.writeUTF(p.name);
	}

	private void searchForFile(Programmer p, Project pj) throws IOException {
		System.out.println("File Search start for " + p.name + " | client number : " + clientNum);
		String name = this.input.readUTF();
		String detail = this.input.readUTF();
		HeadFile hf = null;
		Programmer locker = null;
		ArrayList<HeadFile> list = HeadFile.searchOnTheGo(new HeadFile(name, detail), pj.getFiles());
		this.output.writeInt(list.size());
		for(int i=0;i<list.size();i++){
			hf = list.get(i);
			if(hf.getStatus().equals(FileStatus.Lock))
				locker = hf.findLocker();
			else
				locker = null;
			this.output.writeUTF(hf.Name);
			this.output.writeUTF(hf.getDetail());
			this.output.writeInt(hf.lastVersion.intValue());
			if(hf.getStatus().equals(FileStatus.Lock))
				this.output.writeUTF("Lock");
			else
				this.output.writeUTF("Unlock");
			if(locker!=null && p.equals(locker))
				this.output.writeUTF("Y");
			else
				this.output.writeUTF("N");
		}
		this.output.writeUTF("END");
	}

	private void stealLock(Programmer p, Project pj) throws IOException {
		String name = this.input.readUTF();
		ArrayList<HeadFile> list = HeadFile.search(name, pj.getFiles());
		if(list == null || list.size()==0){
			System.out.println("File not Found in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("NOT_FOUND");
			return ;
		}
		HeadFile hf = list.get(0);
		if(hf.getStatus().equals(FileStatus.Unlock)){
			System.out.println("File "+hf.Name+" is unlock in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			hf.lockThisFile(p);
			System.out.println("File "+hf.Name+" locked in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("SUCCESS");
			return ;
		}
		Programmer locker = hf.findLocker();
		System.out.println("File "+hf.Name+" no longer in "+locker.name+" Lock | client Number : "+clientNum+" Prohrammer : "+p.name);
		hf.unlockThisFile(locker);
		hf.lockThisFile(p);
		System.out.println("File "+hf.Name+" locked in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
		this.output.writeUTF("SUCCESS");
	}

	private void unLockFile(Programmer p, Project pj) throws IOException {
		String name = this.input.readUTF();
		ArrayList<HeadFile> list = HeadFile.search(name, pj.getFiles());
		if(list == null || list.size()==0){
			System.out.println("File not Found in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("NOT_FOUND");
			return ;
		}
		HeadFile hf = list.get(0);
		if(hf.getStatus().equals(FileStatus.Unlock)){
			System.out.println("File "+hf.Name+" isn't lock!! in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("ERROR");
			return ;
		}
		Programmer locker = hf.findLocker();
		if(!p.equals(locker)){
			System.out.println("File "+hf.Name+" isn't in your lock in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("ACCESS_DENIED");
			return ;
		}
		hf.unlockThisFile(p);
		System.out.println("File "+hf.Name+" unlocked in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
		this.output.writeUTF("SUCCESS");
	}

	private void lockFile(Programmer p, Project pj) throws IOException {
		String name = this.input.readUTF();
		ArrayList<HeadFile> list = HeadFile.search(name, pj.getFiles());
		if(list == null || list.size()==0){
			System.out.println("File not Found in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("NOT_FOUND");
			return ;
		}
		HeadFile hf = list.get(0);
		if(hf.getStatus().equals(FileStatus.Lock)){
			System.out.println("File "+hf.Name+" is lock in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("FILE_IS_LOCKED");
			return ;
		}
		hf.lockThisFile(p);
		System.out.println("File "+hf.Name+" locked in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
		this.output.writeUTF("SUCCESS");
	}

	private void commitFile(Programmer p, Project pj) throws IOException {
		String name = this.input.readUTF();
		String newName = this.input.readUTF();
		ArrayList<HeadFile> list = HeadFile.search(name, pj.getFiles());
		if(list == null || list.size()==0){
			System.out.println("File not Found in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("NOT_FOUND");
			return ;
		}
		HeadFile hf = list.get(0);
		if(hf.getStatus().equals(FileStatus.Unlock)){
			System.out.println("File not commitable in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("ERROR");
			return ;
		}
		Programmer locker = hf.findLocker();
		if(!p.equals(locker)){
			System.out.println("File "+hf.Name+" isn't in your lock in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("ACCESS_DENY");
			return ;
		}
		this.output.writeUTF("OK");
		int fileSize = this.input.readInt();
		byte [] fileB = new byte [fileSize];
		this.input.read(fileB);
		FileOutputStream fOut = new FileOutputStream("D:\\WS\\ServerApp\\Files\\" + hf.onlyName().concat(hf.lastVersion.toString()).concat(newName));
		fOut.write(fileB);
		fOut.close();
		File f = new File("D:\\WS\\ServerApp\\Files\\" + hf.onlyName().concat(hf.lastVersion.toString()).concat(newName));
		hf.commit(newName, f, p);
	}

	private void updateFile(Programmer p, Project pj) throws IOException {
		String name = this.input.readUTF();
		ArrayList<HeadFile> list = HeadFile.search(name, pj.getFiles());
		if(list == null || list.size()==0){
			System.out.println("File not Found in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("FILE_NOT_FOUND");
			return ;
		}
		HeadFile hf = list.get(0);
		File f = hf.update(p);
		this.output.writeUTF("OK");
		FileInputStream fIn = new FileInputStream(f);
		byte [] fileB = new byte [(int) f.length()];
		fIn.read(fileB);
		this.output.writeInt((int) f.length());
		this.output.write(fileB);
		fIn.close();
		System.out.println("File "+hf.Name+" Updated for "+p.name+" in "+pj.name+" Project | client Number : "+clientNum);
		this.output.writeUTF("SUCCESS");
	}

	private void newFile(Programmer p, Project pj) throws IOException {
		String fileName = this.input.readUTF();
		String detail = this.input.readUTF();
		ArrayList<HeadFile> list = HeadFile.search(fileName, pj.getFiles());
		if(list != null && list.size()!=0){
			System.out.println("File already exist with this name in " + pj.name + " Project | client Number : " + clientNum + " by name of " + p.name);
			this.output.writeUTF("FILE_NAME_ERROR");
			return ;
		}
		this.output.writeUTF("OK");
		int fileSize = this.input.readInt();
		byte [] fileB = new byte [fileSize];
		this.input.read(fileB);
		FileOutputStream fOut = new FileOutputStream("D:\\WS\\ServerApp\\Files\\" + fileName);
		fOut.write(fileB);
		fOut.close();
		File f = new File("D:\\WS\\ServerApp\\Files\\" + fileName);
		HeadFile hf = HeadFile.newFile(fileName, detail, f, p);
		pj.addFile(hf);
		this.output.writeUTF("SECCESS");
	}

	private void showFile(Project pj) throws IOException {
		ArrayList<HeadFile> list = pj.getFiles();
		for(int i=0;i<list.size();i++){
			this.output.writeUTF(list.get(i).Name);
			this.output.writeUTF(list.get(i).getDetail());
			this.output.writeInt(list.get(i).lastVersion.intValue());
			if(list.get(i).getStatus().equals(FileStatus.Lock)){
				this.output.writeUTF("Lock");
			}
			if(list.get(i).getStatus().equals(FileStatus.Unlock)){
				this.output.writeUTF("Unlock");
			}
		}
		this.output.writeUTF("END");
	}

	private void showMyProj(Programmer p) throws IOException {
		ArrayList<Project> list = p.getMyProj();
		this.output.writeInt(list.size());
		for(int i=0;i<list.size();i++){
			this.output.writeUTF(list.get(i).name);
			this.output.writeUTF(list.get(i).getDetail());
		}
		this.output.writeUTF("END");
	}

	private void newProj(Programmer p) throws IOException {
		System.out.println("Attempting to make a new Project | client Number : " + clientNum + " by name of : " + p.name);
		String name = this.input.readUTF();
		String pass = this.input.readUTF();
		String details = this.input.readUTF();
		ArrayList<Project> list = Project.search(name, this.mainApp.getData().getAllProj());
		if(list != null && list.size()!=0){
			System.out.println("Can't creat a Project with this name | client Number : " + clientNum + " by name of :" + p.name);
			this.output.writeUTF("CANT");
			return ;
		}
		Project pj;
		try {
			pj = Project.newProject(name, pass, details, p);
			p.newProjectCreat(pj);
			this.mainApp.getData().addProj(pj);
			this.output.writeUTF("OK");
			System.out.println("Project Created | client Number : " + clientNum + "by name of : " + p.name);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("Hash Problem");
			this.output.writeUTF("HASH_PROBLEM");
		}
	}

	private void exit() {
		System.out.println("Done! | client Number : " + clientNum);
	}

}