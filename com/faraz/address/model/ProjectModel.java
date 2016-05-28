package com.faraz.address.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProjectModel {
	private final StringProperty name;
	private final StringProperty detail;

	private boolean isPublicOrNot;

	public ProjectModel(String name, String detail, boolean isPublic){
		this.name = new SimpleStringProperty(name);
		this.detail = new SimpleStringProperty(detail);
		this.isPublicOrNot = isPublic;
	}

	public boolean isPublic(){
		return isPublicOrNot;
	}

	public String getDetail(){
		return this.detail.get();
	}

	public void setDetail(String d){
		this.detail.set(d);
	}

	public StringProperty detailProperty(){
		return detail;
	}

	public String getName() {
        return this.name.get();
    }

    public void setName(String Name) {
        this.name.set(Name);
    }

    public StringProperty nameProperty() {
        return name;
    }
}
