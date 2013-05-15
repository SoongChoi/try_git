package com.kt.glos.network;


public interface UrlList {
	String Error = "통신이 원활하지 않습니다.\n잠시후 다시 실행해 주시기 바랍니다.";
	
	/**baas.io Member ID*/
	String ORGANIZATION_ID = "3b283d8a-a010-11e2-bcdf-06ebb80000ba";
	/**baas.io App ID*/
	String APPLICATION_ID = "ce16b9d0-a011-11e2-bd1a-06fd000000c2";
	/**Data Main URL*/
	String GLOS_URL = "https://api.baas.io/"+ORGANIZATION_ID+"/"+APPLICATION_ID+"/";//Test
	String OAUTH_URL = "https://api.baas.io/glos/glos/";
//	final String GLOS_URL = "";//User
	
	/**Push Admin ID*/
	String GCM_SENDER_ID = "";
	/**facebook API Key*/
	String GLOS_FACEBOOK_API_KEY = "358308167608606";
	/**facebook Secret Code*/
	String GLOS_FACEBOOK_SECRET_CODE = "a9c9e305f201a6704c7250265f77539b";
	/**twitter key*/
	String GLOS_TWITTER_KEY = "VNMacsbDpyn5672lojxxA";
	/**twitter secret*/
	String GLOS_TWITTER_SECRET = "uJ2DI2THqCUMBOUltrWXSHYCA0316EF2KitywBaIDuY";
	
	/*데이터타입*/
	String POST = "POST";
	String GET = "GET";
	String PUT = "PUT";
	String DEL = "DELETE";
	
	String limit ="limit";
	
	/**패이스북 로그인**/
	String FacebookOAUTH =  GLOS_URL+"auth/facebook";
	/**메이리스트 요청**/
	String MainList =  OAUTH_URL+"mainlist";
	
	/**메이리스트 상세요청**/
	String DetailList =  OAUTH_URL+"detaillist";

}
