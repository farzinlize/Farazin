package com.faraz.address.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VersioningModel {
	private final StringProperty name;
	private final StringProperty date;
	private final StringProperty maker;

	public VersioningModel(String n, String d, String m){
		this.name = new SimpleStringProperty(n);
		this.date = new SimpleStringProperty(d);
		this.maker = new SimpleStringProperty(m);
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

	public String getDate(){
		return this.date.get();
	}

	public void setDate(String d){
		this.date.set(d);
	}

	public StringProperty dateProperty(){
		return date;
	}

	public String getMaker(){
		return this.maker.get();
	}

	public void setMaker(String m){
		this.maker.set(m);
	}

	public StringProperty makerProperty(){
		return maker;
	}

}
