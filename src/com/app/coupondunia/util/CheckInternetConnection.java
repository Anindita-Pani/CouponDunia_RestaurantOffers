/*
 * 
 */
package com.app.coupondunia.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetConnection {

	ConnectivityManager connectivityManager;
	

	NetworkInfo wifiInfo, mobileInfo;
	

	Context _context;

	public CheckInternetConnection(Context con) {
		// TODO Auto-generated constructor stub
		_context = con;
	}

	// Checking the availability of internet connection using the MobileData or
	
	// Wifi Network
	public Boolean checkNow() {

		try {
			connectivityManager = (ConnectivityManager) _context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifiInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			mobileInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			System.out
					.println("CheckConnectivity Exception: " + e.getMessage());
		}

		return false;
	}
}