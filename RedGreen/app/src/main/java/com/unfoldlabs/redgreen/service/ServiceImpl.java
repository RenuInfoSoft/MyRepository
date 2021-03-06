package com.unfoldlabs.redgreen.service;

import android.os.AsyncTask;

import com.google.gson.JsonObject;
import com.unfoldlabs.redgreen.config.AWSConstants;
import com.unfoldlabs.redgreen.log.Applog;
import com.unfoldlabs.redgreen.model.AnalyticalsInfo;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to Service implementation to interact with GCM server
 *
 */
@SuppressWarnings({ "deprecation" })
public class ServiceImpl {
	JSONObject jsonObj;
	/**
	 * Method to Register and get User id with GCM
	 * @param registerMap
	 * @return jsonObject
	 */
	public JSONObject registerUserId(Map<String, Object> registerMap) {

		JsonObject json = new JsonObject();
		json.addProperty("emailId", (String) registerMap.get("emailId"));
		json.addProperty("mobileNo", (String) registerMap.get("mobileNo"));
		json.addProperty("imeiNo", (String) registerMap.get("imeiNo"));
		json.addProperty("address", (String) registerMap.get("address"));
		String builder = sendPostMethod(
				AWSConstants.REGISTER_URL, json);
		try {
			jsonObj = new JSONObject(builder);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
	/**
	 * Method to Register device with GCM
	 * @param registerMap
	 * @return jsonObject
	 */
	public JSONObject registerDeviceId(Map<String, Object> registerMap, Integer notificationStatus) {

		JsonObject json = new JsonObject();
		json.addProperty("deviceRegId", (String) registerMap.get("deviceRegId"));
		json.addProperty("notificationStatus", notificationStatus);
		json.addProperty("userId", (Integer) registerMap.get("userId"));
		json.addProperty("imeiNo", (String)registerMap.get("imeiNo"));
		String builder = sendPostMethod(
				AWSConstants.DEVICE_INFO, json);


		try {
			jsonObj = new JSONObject(builder);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}
	/**
	 * Method to register AWS analytics Data
	 * @param analyticalInfo
	 */
	public void registerAnalyticalDetails(JsonObject analyticalInfo) {

		new AsyncRegisterAnalytics(analyticalInfo)
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



	}
	/**
	 * Class for store data in DataBase
	 *
	 */
	public class AsyncRegisterAnalytics extends
			AsyncTask<Void, Void, JSONObject> {


		private JsonObject analyticalInfo;

		public AsyncRegisterAnalytics(JsonObject result) {
			analyticalInfo = result;
		}



		@Override
		protected JSONObject doInBackground(Void... params) {
			String url = "http://52.26.73.211:8080/unfoldlabs-api/rest/user/analyticals";
			String builder = sendPostMethodAnalytical(url, analyticalInfo);

			try {
				Pattern p = Pattern.compile("^.*?\\((.*?)\\);$", Pattern.DOTALL);
				Matcher m = p.matcher(builder);
				if (m.matches()) {
					String json = m.group(1);
					jsonObj = new JSONObject(json);
				} else {
					jsonObj = new JSONObject(builder);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return jsonObj;
		}


		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
		}

	}

	/**
	 * Method to send the Analytical data through json web services
	 * @param url
	 * @param json
	 * @return builder
	 */
	public String sendPutMethod(String url, JsonObject json) {

		Header[] headers = {
				new BasicHeader("Content-type", "application/json"),
				new BasicHeader("Accept", "application/json") };

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		try {
			HttpPost httpPut = new HttpPost(url);
			httpPut.setHeaders(headers);
			Header header = null;
			httpPut.setHeader(header);
			HttpEntity e = new StringEntity(json.toString());
			httpPut.setEntity(e);
			org.apache.http.HttpResponse response = client.execute(httpPut);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Applog.logString("==> Failed to execute Post Method");
			}

		} catch (ClientProtocolException ee) {
			ee.printStackTrace();
		} catch (IOException eee) {
			eee.printStackTrace();
		}
		return builder.toString();
	}

	/**
	 * Method to get the Analytical data through json web services
	 * @param url
	 * @return
	 */
	public String sendGetMethod(String url) {

		Header[] headers = {
				new BasicHeader("Content-type", "application/json"),
				new BasicHeader("Accept", "application/json") };

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeaders(headers);
			Header header = null;
			httpGet.setHeader(header);
			org.apache.http.HttpResponse response = client.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Applog.logString("==> Failed to execute Get Method File");
			}

		} catch (ClientProtocolException ee) {
			ee.printStackTrace();
		} catch (IOException eee) {
			eee.printStackTrace();
		}
		return builder.toString();
	}

	/**
	 * Method to post the data through json web services0
	 * @param url
	 * @return
	 */
	public String sendPostMethod(String url1, JsonObject json) {
		StringBuilder builder = new StringBuilder();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url1);
		try {
			Header[] headers = {
					new BasicHeader("Content-type", "application/json"),
					new BasicHeader("Accept", "application/json") };

			httpPost.setHeaders(headers);
			HttpEntity e = new StringEntity(json.toString());
			httpPost.setEntity(e);
			org.apache.http.HttpResponse response = httpclient
					.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			if (statusCode == 200) {
				builder.toString();

			} else if (statusCode == 400) {
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} // Parse String to JSON objec
		catch (NullPointerException e) {
			e.printStackTrace();
		} //
		return builder.toString();
	}
	/**
	 * Method to post the Analytical data through json web services
	 * @param url
	 * @return
	 */
	public String sendPostMethodAnalytical(String url1, JsonObject jsonObj) {
		StringBuilder builder = new StringBuilder();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url1);
		try {
			Header[] headers = {
					new BasicHeader("Content-type", "application/json"),
					new BasicHeader("Accept", "application/json") };


			httpPost.setHeaders(headers);
			HttpEntity e = new StringEntity(jsonObj.toString());
			httpPost.setEntity(e);
			org.apache.http.HttpResponse response = httpclient
					.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			if (statusCode == 200) {

				builder.toString();

			} else if (statusCode == 400) {

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} // Parse String to JSON objec
		catch (NullPointerException e) {
			e.printStackTrace();
		} // Parse String to JSON objec
		return builder.toString();
	}

}