package com.farzin.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class HeadFile implements Serializable{
	private FileStatus status;
	public String Name;
	private String directory;
	private String detail;
	private File file;
	private ArrayList<Activity> history;
	public Integer lastVersion;

	public HeadFile(String name, String detail){
		this.Name = name;
		this.detail = detail;
	}

	public ArrayList<Activity> getHistory(){
		return history;
	}

	public HeadFile(String name, String detail, File f){
		this.status = FileStatus.Unlock;
		this.file = f;
		this.Name = name;
		this.detail = detail;
		this.lastVersion = 0;
		this.history = new ArrayList<Activity>();
	}

	public String getDetail(){
		return detail;
	}

	public String onlyName(){
		int i;
		for(i=0;i<this.Name.length();i++){
			if(this.Name.charAt(i)=='.')
				break ;
		}
		return this.Name.substring(0, i);
	}

	public String onlyType(){
		int i;
		for(i=0;i<this.Name.length();i++){
			if(this.Name.charAt(i)=='.')
				break ;
		}
		return this.Name.substring(i, this.Name.length());
	}

	public Programmer findLocker(){
		int i;
		Activity a = null , last = null;
		for(i=0;i<history.size();i++)
			if(history.get(i).type.equals(ActivityType.lock))
				break ;
		if(i==history.size())
			throw new IllegalArgumentException("File isn't Lock");
		last = history.get(i);
		for(i++;i<history.size();i++){
			a = history.get(i);
			if(a.type.equals(ActivityType.lock)){
				if(a.date.after(last.date))
					last = a;
			}
		}
		return last.getMaker();
	}

	public FileStatus getStatus(){
		return status;
	}

	public String getDirectory(){
		return directory;
	}

	public synchronized File update(Programmer maker){
		Activity a = new Activity(ActivityType.update, maker);
		a.date = new Date();
		history.add(a);
		return file;
	}

	public synchronized File downloadVersion(Programmer maker, String verName){
		ArrayList<Versioning> list = this.fileVersions();
		Versioning v = null;
		for(int i=0;i<list.size();i++)
			if(list.get(i).Name.equals(verName))
				v = list.get(i);
		if(v==null)
			return null;
		Activity a = new Activity(ActivityType.downloadVersion, maker);
		a.date = new Date();
		history.add(a);
		return v.getFile();
	}

	public synchronized void lockThisFile(Programmer maker){
		Activity a = new Activity(ActivityType.lock, maker);
		a.date = new Date();
		history.add(a);
		status = FileStatus.Lock;
	}

	public void unlockThisFile(Programmer maker){
		Activity a = new Activity(ActivityType.unLock, maker);
		a.date = new Date();
		history.add(a);
		status = FileStatus.Unlock;
	}

	public Programmer findPrevProgrammer(){
		ArrayList<Versioning> list = this.fileVersions();
		if(list==null || list.size()==0){
			Activity a;
			for(int i=0;i<this.history.size();i++){
				a = this.history.get(i);
				if(a.type.equals(ActivityType.newFile))
					return a.getMaker();
			}
		}
		Versioning v, last = list.get(0);
		for(int i=1;i<list.size();i++){
			v = list.get(i);
			if(v.date.after(last.date))
				last = v;
		}
		return last.getMaker();
	}

	public void commit(String name, File f, Programmer maker){
		Programmer prePro = this.findPrevProgrammer();
		Versioning v = new Versioning(maker, ActivityType.commit, file, prePro);
		v.Name = name;
		this.lastVersion++;
		v.date = new Date();
		this.history.add(v);
		this.file = f;
	}

	public synchronized void move(String path, Programmer maker){
		Activity a = new Activity(ActivityType.move, maker);
		a.date = new Date();
		history.add(a);
		this.directory = path;
	}

	public File getFile(){
		return this.file;
	}

	public static ArrayList<HeadFile> search(String name, ArrayList<HeadFile> DBF){
		ArrayList<HeadFile> list = new ArrayList<HeadFile>();
		HeadFile hf1 = null;
		for(int i=0;i<DBF.size();i++){
			hf1 = DBF.get(i);
			if(hf1.Name.equals(name))
				list.add(hf1);
		}
		return list;
	}

	public static ArrayList<HeadFile> searchOnTheGo(HeadFile hf, ArrayList<HeadFile> DBF){
		ArrayList<HeadFile> list = new ArrayList<HeadFile>();
		HeadFile hf1 = null;
		for(int i=0;i<DBF.size();i++){
			hf1 = DBF.get(i);
			if(hf1.Name.contains(hf.Name)){
				list.add(hf1);
				continue ;
			}
			if(hf1.detail.contains(hf.detail)){
				list.add(hf1);
				continue ;
			}
		}
		return list;
	}

	public static HeadFile newFile(String name, String detail, File f, Programmer maker){
		HeadFile hf = new HeadFile(name, detail, f);
		Activity a = new Activity(ActivityType.newFile, maker);
		a.date = new Date();
		hf.history.add(a);
		return hf;
	}

	public ArrayList<Versioning> fileVersions(){
		ArrayList<Versioning> versions = new ArrayList<Versioning>();
		for(int i=0 ; i<this.history.size();i++){
			if(this.history.get(i).type.equals(ActivityType.commit))
				versions.add((Versioning) this.history.get(i));
		}
		return versions;
	}
}
