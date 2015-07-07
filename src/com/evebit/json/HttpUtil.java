package com.evebit.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.content.Context;
import android.util.Log;


/**
 * Http??????ï¿½é??ï¿?
 * 
 * @author Administrator
 * 
 */
public class HttpUtil {

	/**
	 * Get?????¤æ???????????¤æ???????ï¿½é??ï¿½æ??è¯§æ????????¤æ????
	 * 
	 * @param context
	 * @param url
	 * @return ???ï¿½é??ï¿½é??ï¿?
	 * @throws Y_Exception
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String httpGet(Context context, String url) throws Y_Exception {

		try {
			return EntityUtils.toString(httpGetHttpEntity(context, url), HTTP.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get?????¤æ???????????¤æ???????ï¿????????¤æ????
	 * 
	 * @param url
	 * @return
	 * @throws Y_Exception
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static InputStream httpGetInputStream(Context context, String url) throws Y_Exception {

		try {
			return httpGetHttpEntity(context, url).getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpEntity httpGetHttpEntity(Context context, String url) throws Y_Exception {
		
		try {

		
			
			URL urls = new URL(url);
			URI uri = new URI(urls.getProtocol(), urls.getHost(), urls.getPath(), urls.getQuery(), null);
			HttpGet httpget = new HttpGet(uri);

			HttpClient httpclient = new DefaultHttpClient();

			HttpResponse httpResponse;

			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);

			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
						
			httpResponse = httpclient.execute(httpget);
			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				return httpResponse.getEntity();
			} else {
				throw Y_Exception.http(statusCode);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			throw Y_Exception.network(e);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw Y_Exception.network(e);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw Y_Exception.network(e);
		}

	}

	/**
	 * Post?????¤æ???????????¤æ???????ï¿½é??ï¿½æ??è¯§æ????????¤æ????
	 * 
	 * @return
	 * @throws Y_Exception
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String httpPost(Context context, String url, List<NameValuePair> params) throws Y_Exception {

		try {
			return EntityUtils.toString(httpPostHttpEntity(context, url, params));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author hubaolin
	 * @param postUrl
	 * @param params
	 * @return
	 * @throws Y_Exception
	 */

	public static String doPost(String postUrl, List<BasicNameValuePair> params) throws Y_Exception {
		String result = null;
		HttpPost httpRequest = null;
		HttpResponse httpResponse = null;
		try {
			
			httpRequest = new HttpPost(postUrl);
			
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { //
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			throw Y_Exception.network(e);
		}
		return result;

	}

	/**
	 * 
	 * @author hubaolin
	 * @throws Y_Exception
	 * @POST ???????¤æ?????????¤æ????;
	 * 
	 * @postUrl post?????¤æ???????URL;
	 * 
	 * @params ???ï¿½ç????????????????
	 */
	public static String doPost(String postUrl, Map<String, String> params) throws Y_Exception {
		String result = null;
		HttpPost httpRequest = null;
		HttpResponse httpResponse = null;
		List<BasicNameValuePair> listPara = null;
		if (null != params) {
			listPara = new ArrayList<BasicNameValuePair>();
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				listPara.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
		}
		try {
			httpRequest = new HttpPost(postUrl);
			//httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			httpRequest.setEntity(new UrlEncodedFormEntity(listPara, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { //
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			throw Y_Exception.network(e);
		}
		return result;

	}

	/**
	 * Post?????¤æ???????????¤æ???????ï¿????????¤æ????
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static InputStream httpPostInputStream(Context context, String url, List<NameValuePair> params) throws Y_Exception,
			IllegalStateException, IOException {

		return httpPostHttpEntity(context, url, params).getContent();
	}

	public static HttpEntity httpPostHttpEntity(Context context, String url, List<NameValuePair> params) throws Y_Exception {
		
		HttpPost httppost = new HttpPost(url);	
		
		try {
			HttpEntity Httpentity = new UrlEncodedFormEntity(params, "utf-8");
			httppost.setEntity(Httpentity);
			HttpClient hc = new DefaultHttpClient();
			
			// ?????¤æ???????ï¿????
			hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            // ???ä¼?????????ï¿????
			hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);           
      
			HttpResponse httpResponse = hc.execute(httppost);         
			
			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return httpResponse.getEntity();
			} else {
				throw Y_Exception.http(statusCode);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			throw Y_Exception.network(e);
		}
	}
}
