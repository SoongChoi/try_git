package com.kt.glos.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.kt.glos.util.LogTag;

public class UserItem {

	String jsonData;
	String access_token;
	String expires_in;
	String user;
	String facebook;

	UserDetailItem userDetailItem = null;

	public UserItem(String jsonData) {
		this.jsonData = jsonData;
		try
		{
			JSONObject order = new JSONObject(jsonData);
			access_token = order.has("access_token")?order.getString("access_token"):"";
			expires_in = order.has("expires_in")?order.getString("expires_in"):"";
			user = order.has("user")?order.getString("user"):"";
			facebook = order.has("facebook")?order.getString("facebook"):"";
		}
		catch (JSONException e) {
			// TODO: handle exception
			LogTag.e("glos",e.toString());
		}

		if(!"".equals(user))
		{
			userDetailItem = new UserDetailItem(user);
		}
	}

	public class UserDetailItem {
		String jsonData;
		String uuid;
		String type;
		String created;
		String modified;
		String username;
		String email;
		String activated;
		String picture;
		public UserDetailItem(String jsonData){
			this.jsonData = jsonData;
			try
			{
				JSONObject order = new JSONObject(jsonData);
				uuid = order.has("uuid")?order.getString("uuid"):"";
				type = order.has("type")?order.getString("type"):"";
				created = order.has("created")?order.getString("created"):"";
				modified = order.has("modified")?order.getString("modified"):"";
				username = order.has("username")?order.getString("username"):"";
				email = order.has("email")?order.getString("email"):"";
				activated = order.has("activated")?order.getString("activated"):"";
				picture = order.has("picture")?order.getString("picture"):"";
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
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getCreated() {
			return created;
		}
		public void setCreated(String created) {
			this.created = created;
		}
		public String getModified() {
			return modified;
		}
		public void setModified(String modified) {
			this.modified = modified;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getActivated() {
			return activated;
		}
		public void setActivated(String activated) {
			this.activated = activated;
		}
		public String getPicture() {
			return picture;
		}
		public void setPicture(String picture) {
			this.picture = picture;
		}
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public UserDetailItem getUserDetailItem() {
		return userDetailItem;
	}

	public void setUserDetailItem(UserDetailItem userDetailItem) {
		this.userDetailItem = userDetailItem;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

}
