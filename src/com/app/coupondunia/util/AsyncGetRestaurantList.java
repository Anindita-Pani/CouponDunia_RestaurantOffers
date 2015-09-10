package com.app.coupondunia.util;

/* Async Task to load restaurant list */
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;

import com.google.gson.Gson;

public class AsyncGetRestaurantList extends
		AsyncTask<String, String, List<BeanRestaurantDetails.Data>> {

	int msgCode;
	RestaurantDownloadListner lisner;
	BeanRestaurantDetails storeDtls;

	@Override
	protected List<BeanRestaurantDetails.Data> doInBackground(String... params) {
		// TODO Auto-generated method stub
		List<BeanRestaurantDetails.Data> storeList = new ArrayList<BeanRestaurantDetails.Data>();
		BeanRestaurantDetails.Data dataObj;
		try {

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 40000; // time out in milliseconds
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 40000; // time out in milliseconds for waiting
										// for data.
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);

			HttpGet httppget = new HttpGet(AppConstants.URL_STORE_DTLS);
			HttpResponse response = httpclient.execute(httppget);
			HttpEntity entity = response.getEntity();

			Reader reader = new InputStreamReader(entity.getContent());
			// use gson to get object with the help of object mapper
			// class-BeanStoreDetails
			Gson gson = new Gson();
			storeDtls = gson.fromJson(reader, BeanRestaurantDetails.class);
			
			// check if status from json data is OK(considering 200 is code for OK)
			if(storeDtls.getStatus().getRcode().equals("200"))
			{
				Set<String> keys = storeDtls.data.keySet();
				for (String key : keys) {
					dataObj = storeDtls.data.get(key);
					storeList.add(dataObj);
				}

				msgCode = AppConstants.LIST_ITEMS_SUCCESS_CODE;
			}
			else
				msgCode = AppConstants.LIST_ITEMS_ERROR_CODE;
			
			
		} catch (Exception e) {
			msgCode = AppConstants.LIST_ITEMS_ERROR_CODE;
		}
		return storeList;
	}

	@Override
	protected void onPostExecute(List<BeanRestaurantDetails.Data> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// set the listener method call
		lisner.GetStoreList(result, msgCode);
	}

	// set listener
	public void SetListner(RestaurantDownloadListner lisner) {
		this.lisner = lisner;
	}

	// interface for listener
	public interface RestaurantDownloadListner {
		public void GetStoreList(List<BeanRestaurantDetails.Data> storeList,
				int msgCode);
	}
}
