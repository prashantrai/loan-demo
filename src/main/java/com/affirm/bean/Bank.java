package com.affirm.bean;

import java.io.Serializable;

public class Bank implements Serializable{
	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Bank [id=" + id + ", name=" + name + "]";
	}
	
}
