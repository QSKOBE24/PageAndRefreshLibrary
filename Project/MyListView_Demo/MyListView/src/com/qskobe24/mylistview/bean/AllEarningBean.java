package com.qskobe24.mylistview.bean;

public class AllEarningBean {

	private String dateString;
	private String allMoneyString;
	private String earningPerDayString;
	
	
	
	public AllEarningBean(String dateString, String allMoneyString,
			String earningPerDayString) {
		this.dateString = dateString;
		this.allMoneyString = allMoneyString;
		this.earningPerDayString = earningPerDayString;
		
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getAllMoneyString() {
		return allMoneyString;
	}
	public void setAllMoneyString(String allMoneyString) {
		this.allMoneyString = allMoneyString;
	}
	public String getEarningPerDayString() {
		return earningPerDayString;
	}
	public void setEarningPerDayString(String earningPerDayString) {
		this.earningPerDayString = earningPerDayString;
	}
	
	
}
