package com.farzin.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Activity implements Serializable{
	public Date date;
	public ActivityType type;
	private Programmer maker;

	public Activity(ActivityType t, Programmer p){
		this.maker = p;
		this.type = t;
	}

	public Programmer getMaker(){
		return maker;
	}
}
