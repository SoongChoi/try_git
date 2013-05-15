package com.kt.glos.network;

public interface NetWorkComplete {
	public String contentType="Content-Type: image/jpeg";
	public String disposition="Content-Disposition: form-data; name=";
	public String boundary="-----------------------------7db3b9140e84"; 
	public void onNetWorkComplete(boolean isSuccess,String jsonData);
}
