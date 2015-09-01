package com.qskobe24.mylistview.bean;

public class DealFlowBean {
	
	private String    dateString;
	private String    dealTypeString;
	private String    dealMoneyString;
	private String    dealStatusString;
	
	public DealFlowBean(String dateString,String dealTypeString, String dealMoneyString,
			String dealStatusString) {
		this.dateString = dateString;
		
		this.dealTypeString = dealTypeString;
		this.dealMoneyString = dealMoneyString;
		this.dealStatusString = dealStatusString;
		
	}

	public String getDealTypeString() {
		return dealTypeString;
	}

	public void setDealTypeString(String dealTypeString) {
		this.dealTypeString = dealTypeString;
	}

	public String getDealMoneyString() {
		return dealMoneyString;
	}

	public void setDealMoneyString(String dealMoneyString) {
		this.dealMoneyString = dealMoneyString;
	}

	public String getDealStatusString() {
		return dealStatusString;
	}

	public void setDealStatusString(String dealStatusString) {
		this.dealStatusString = dealStatusString;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
	
}
