package com.kt.glos.twitter;

import com.kt.glos.network.UrlList;

public interface TwitterConstant{
	public  String GLOS_TWITTER_CONSUMER_KEY = UrlList.GLOS_TWITTER_KEY;
	public  String GLOS_TWITTER_CONSUMER_SECRET = UrlList.GLOS_TWITTER_SECRET;
	public  String TWITTER_CALLBACK_URL = "http://baas.io";

	public  String MOVE_TWITTER_LOGIN = "com.android.twittercon,TWITTER_LOGIN";
	public  int TWITTER_LOGIN_CODE = 10;
	public  String TWITTER_LOGOUT_URL = "http://twitter.com/logout";

	public  String TWITTER_ACCESS_TOKEN = "twitter_access_token";
	public  String TWITTER_ACCESS_TOKEN_SECRET = "twitter_access_token_secret";

	public  boolean D = true;
}
