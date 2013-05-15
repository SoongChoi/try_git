package com.kt.glos.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.kt.glos.util.LogTag;

public class FaceBookItem {
	String jsonData;
	String id;
	String name;
	String first_name;
	String last_name;
	String link;
	String username;
	String gender;
	String timezone;
	String locale;
	String verified;
	String updated_time;


	public FaceBookItem(String jsonData) {
		this.jsonData = jsonData;
		try
		{
			JSONObject order = new JSONObject(jsonData);
			name = order.has("name")?order.getString("name"):"";
			first_name = order.has("first_name")?order.getString("first_name"):"";
			last_name = order.has("last_name")?order.getString("last_name"):"";
			link = order.has("link")?order.getString("link"):"";
			username = order.has("username")?order.getString("username"):"";
			gender = order.has("gender")?order.getString("gender"):"";
			timezone = order.has("timezone")?order.getString("timezone"):"";
			locale = order.has("locale")?order.getString("locale"):"";
			verified = order.has("verified")?order.getString("verified"):"";
			updated_time = order.has("updated_time")?order.getString("updated_time"):"";
		}
		catch (JSONException e) {
			// TODO: handle exception
			LogTag.e("glos", e.toString());
		}

	}


	public String getJsonData() {
		return jsonData;
	}


	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFirst_name() {
		return first_name;
	}


	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}


	public String getLast_name() {
		return last_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getTimezone() {
		return timezone;
	}


	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}


	public String getLocale() {
		return locale;
	}


	public void setLocale(String locale) {
		this.locale = locale;
	}


	public String getVerified() {
		return verified;
	}


	public void setVerified(String verified) {
		this.verified = verified;
	}


	public String getUpdated_time() {
		return updated_time;
	}


	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}
	
	
}
