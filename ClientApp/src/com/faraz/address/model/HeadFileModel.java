package com.faraz.address.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HeadFileModel {
	private final StringProperty name;
	private final StringProperty detail;
	private final StringProperty directory;
	private final StringProperty status;
	private final StringProperty amILocker;
	private final IntegerProperty lastVersion;

	public HeadFileModel(String n, String detail, String d, int lv, String s, String am){
		this.name = new SimpleStringProperty(n);
		this.detail = new SimpleStringProperty(detail);
		this.directory = new SimpleStringProperty(d);
		this.lastVersion = new SimpleIntegerProperty(lv);
		this.status = new SimpleStringProperty(s);
		this.amILocker = new SimpleStringProperty(am);
	}

	public String getName(){
		return this.name.get();
	}

	public void setName(String n){
		this.name.set(n);
	}

	public StringProperty nameProperty(){
		return name;
	}

	public String getDirectory(){
		return this.directory.get();
	}

	public void setDirectory(String d){
		this.directory.set(d);
	}

	public StringProperty directoryProperty(){
		return directory;
	}

	public String getStatus(){
		return this.status.get();
	}

	public void setStatus(String s){
		this.status.set(s);
	}

	public StringProperty statusProperty(){
		return status;
	}

	public String getDetail(){
		return this.detail.get();
	}

	public void setDetail(String s){
		this.detail.set(s);
	}

	public StringProperty detailProperty(){
		return detail;
	}

	public int getLastVersion(){
		return lastVersion.get();
	}

	public void setLastVersion(int lv){
		this.lastVersion.set(lv);
	}

	public IntegerProperty lastVersionProperty(){
		return lastVersion;
	}

	public String getAm(){
		return this.amILocker.get();
	}

	public void setAm(String am){
		this.amILocker.set(am);
	}

	public StringProperty AmProperty(){
		return amILocker;
	}

}
