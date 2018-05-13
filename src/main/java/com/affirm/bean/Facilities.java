package com.affirm.bean;

public class Facilities {
	private int id;
	private float amt;
	private float interestRate;
	private int bank_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getAmt() {
		return amt;
	}
	public void setAmt(float amt) {
		this.amt = amt;
	}
	public float getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}
	public int getBank_id() {
		return bank_id;
	}
	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	
	public String toString() {
		
		return "("+ "id="+id + ", amt="+ amt + ", interestRate="+interestRate + ", bank_id="+bank_id + ")";
	}
	
}
