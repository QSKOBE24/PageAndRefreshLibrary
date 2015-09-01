package com.qskobe24.mylistview.bean;

public class CancelDepositBean {

	private String dateString;
	private String typeString;
	private String moneyString;
	private String statusString;
	private String operationString;
	
	public CancelDepositBean(String dateString, String typeString,
			String moneyString, String statusString, String operationString) {
		this.dateString = dateString;
		this.typeString = typeString;
		this.moneyString = moneyString;
		this.statusString = statusString;
		this.operationString = operationString;
	}
	
	
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getTypeString() {
		return typeString;
	}
	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}
	public String getMoneyString() {
		return moneyString;
	}
	public void setMoneyString(String moneyString) {
		this.moneyString = moneyString;
	}
	public String getStatusString() {
		return statusString;
	}
	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}
	public String getOperationString() {
		return operationString;
	}
	public void setOperationString(String operationString) {
		this.operationString = operationString;
	}
	
	
}
