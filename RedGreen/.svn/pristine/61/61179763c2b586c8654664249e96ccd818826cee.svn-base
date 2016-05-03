package com.unfoldlabs.redgreen.config;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AWSServiceConfig {
	Properties props = new Properties();
	InputStream inputStream = this.getClass().getClassLoader()
			.getResourceAsStream("");

	public AWSServiceConfig(AssetManager assetManager) {
		try {
			InputStream inputStream = assetManager.open("omservice.properties");
			props.load(inputStream);
			AWSConstants.PROJECT_ID = (String) props.get("PROJECT_ID");
			AWSConstants.DEVICE_INFO = (String) props.get("DEVICE_INFO");
			AWSConstants.GCM_URL = (String) props.get("GCM_URL");
			AWSConstants.REGISTER_URL = (String) props.get("REGISTER_URL");
			AWSConstants.ANALYTICALS_INFO = (String) props.get("ANALYTICALS_INFO");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
