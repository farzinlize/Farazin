package com.farzin.model;

import java.io.File;

@SuppressWarnings("serial")
public class Versioning extends Activity {
	public String Name;
	private File oldFile;
	private Programmer preProgrammer;

	public Versioning(Programmer maker, ActivityType t, File f, Programmer prePro) {
		super(t, maker);
		this.oldFile = f;
		this.preProgrammer = prePro;
	}

	public Programmer getPrePro(){
		return preProgrammer;
	}

	public File getFile(){
		return oldFile;
	}

}