package com.unfoldlabs.redgreen.interfaces;

public interface Constants {

	String DB_NAME = "redgreen.sqlite";
	int APP_UNINSTALL_REQUEST = 100;
	

	int RECOMENDED_RESULT_CODE = 100;
	int CLEANAPP_RESULT_CODE = 101;
	int APPLOCK_RESULT_CODE = 102;
	
	int DEFAULT_SELECTION_DAYS = 0;

	String share = "I tried RedGreen on Android and it is awesome  thought you would love it too  download it at https://play.google.com/store/apps/details?id=com.unfoldlabs.redgreen&hl=en";

	boolean IGNORE_SERVICE_FRONT_APP = false;

	int SECURITY_LEVEL = 0;
	int SECURITY_LEVEL_HIGH = 0;
	int SECURITY_LEVEL_LOW = 10;
	int SECURITY_LEVEL_MEDIUM = 5;

	/** Applocl constants **/
	String MyPREFERENCES = "REDPrefs" ;
	boolean DEBUG = false;
	int APPS_PER_INTERSTITIAL = 1;
	String POSTURL = "http://52.26.73.211:8080/unfoldlabs-api/rest/user/applock";
	String MATHES = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z ]{2,})$";


	int mNotificationId = 001;
}
