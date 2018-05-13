package com.affirm.bean;

import java.io.Serializable;

public class Covenants implements Serializable{
	private int facilityId;
	private float maxDefault;
	private int bankId;
	private String bannedState;
	public int getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}
	public float getMaxDefault() {
		return maxDefault;
	}
	public void setMaxDefault(float maxDefault) {
		this.maxDefault = maxDefault;
	}
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public String getBannedState() {
		return bannedState;
	}
	public void setBannedState(String bannedState) {
		this.bannedState = bannedState;
	}
	@Override
	public String toString() {
		return "Covenants [facilityId=" + facilityId + ", maxDefault=" + maxDefault + ", bankId=" + bankId
				+ ", bannedState=" + bannedState + "]";
	}
	
}
