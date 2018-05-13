package com.affirm.bean;

public class Loans {

	private int id;
	private float interestRate;
	private float amt;
	private float defaultLikelyhood;
	private String state;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}
	public float getAmt() {
		return amt;
	}
	public void setAmt(float amt) {
		this.amt = amt;
	}
	public float getDefaultLikelyhood() {
		return defaultLikelyhood;
	}
	public void setDefaultLikelyhood(float defaultLikelyhood) {
		this.defaultLikelyhood = defaultLikelyhood;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
