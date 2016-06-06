package com.faraz.address.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ActivityModel {

	private final StringProperty type;
	private final StringProperty prog;
	private final StringProperty date;

	public ActivityModel(String t, String p, String d){
		type = new SimpleStringProperty(t);
		prog = new SimpleStringProperty(p);
		date = new SimpleStringProperty(d);
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

	public String getProg(){
		return this.prog.get();
	}

	public void setProg(String p){
		this.prog.set(p);
	}

	public StringProperty progProperty(){
		return prog;
	}

	public String getType(){
		return this.type.get();
	}

	public void setType(String t){
		this.type.set(t);
	}

	public StringProperty typeProperty(){
		return type;
	}

}
