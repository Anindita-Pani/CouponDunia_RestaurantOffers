package com.app.coupondunia.util;
/* Service to fetch the address from current location's latitude and longitude */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

public class ServiceGetAddress extends IntentService {
	ArrayList<String> addrFrag;

	public ServiceGetAddress() {
		super("name");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String res; //response to broadcast
		Geocoder geocode = new Geocoder(this, Locale.getDefault());
		Location loc = (Location) intent.getParcelableExtra(AppConstants.LOCATION); // location to geocode
		List<Address> addrList = null;

		try {
			addrList = geocode.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);
		} catch (IOException ioException) {

		} catch (IllegalArgumentException illegalArgumentException) {

		}
		if (addrList == null || addrList.size() == 0) {
			res = "";
		
		} else {
			Address addr = addrList.get(0);
			addrFrag = new ArrayList<String>();
			for (int i = 0; i < addr.getMaxAddressLineIndex(); i++)
				addrFrag.add(addr.getAddressLine(i));
			
			res=TextUtils.join(
							", ", addrFrag);
		
		}

		Intent broadcastAddr = new Intent(AppConstants.ADDRESS_INTENT_FILTER);
		broadcastAddr.putExtra(AppConstants.ADDRESS, res);
		// broadcast response
		LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastAddr);
		//stop service
		stopSelf();
		
	}

	
}
