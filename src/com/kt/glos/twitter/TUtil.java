package com.kt.glos.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TUtil {
	
	public final String SPNAME = "Twitterkey";
	public final String tokenKEY = "TWITTER_ACCESS_TOKEN";
	public final String secretTokenKEY = "TWITTER_ACCESS_TOKEN_SECRET";
	
	public void setAppPreferences(Activity context, String oauth_token,String oauth_verifier) {
		SharedPreferences pref = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();

		prefEditor.putString(tokenKEY, oauth_token);
		prefEditor.putString(secretTokenKEY, oauth_verifier);
		prefEditor.commit();
	}
	public String getToken(Activity activity) {
		String returnValue = null;

		SharedPreferences pref = activity.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
		returnValue = pref.getString(tokenKEY, "");
		return returnValue;
	}
	public String getSecretTokenKey(Activity activity) {
		String returnValue = null;

		SharedPreferences pref = activity.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
		returnValue = pref.getString(secretTokenKEY, "");
		return returnValue;
	}
	public void clear(Context context) {
        Editor editor = 
            context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
	public boolean checkToken(Activity activity)
	{
		boolean isExist = false;
		SharedPreferences pref = activity.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
		String token = pref.getString(tokenKEY, "");
		
		String secretToken = pref.getString(secretTokenKEY, "");
		if(token.length()>0 && secretToken.length()>0) isExist= true;
		
		return isExist;
	}

}
