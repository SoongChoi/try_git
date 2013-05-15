package com.kt.glos.textutil;

import java.io.Serializable;

public class AutoTextDBadapter implements Serializable{
	private static final long serialVersionUID = 123456789L;
	String date;
	String searchText;
	
	public void setData(String date, String searchText){
		this.date = date;
		this.searchText = searchText;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

}
