package com.qskobe24.mylistview.bean;

public class ProductEarningBean {
	private String dateString;
	private String earningInTenThousandString;
	private String sevenDaysInterestRatesString;
	
	public ProductEarningBean(String dateString,
			String earningInTenThousandString,
			String sevenDaysInterestRatesString) {
		this.dateString = dateString;
		this.earningInTenThousandString = earningInTenThousandString;
		this.sevenDaysInterestRatesString = sevenDaysInterestRatesString;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getEarningInTenThousandString() {
		return earningInTenThousandString;
	}
	public void setEarningInTenThousandString(String earningInTenThousandString) {
		this.earningInTenThousandString = earningInTenThousandString;
	}
	public String getSevenDaysInterestRatesString() {
		return sevenDaysInterestRatesString;
	}
	public void setSevenDaysInterestRatesString(String sevenDaysInterestRatesString) {
		this.sevenDaysInterestRatesString = sevenDaysInterestRatesString;
	}
	
	
}
