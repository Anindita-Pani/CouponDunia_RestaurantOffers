package com.app.coupondunia;

/*This activity is used to display the enitire list of restaurants*/

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.coupondunia.util.AdapterRestaurantDetail;
import com.app.coupondunia.util.AppConstants;
import com.app.coupondunia.util.AsyncGetRestaurantList;
import com.app.coupondunia.util.AsyncGetRestaurantList.RestaurantDownloadListner;
import com.app.coupondunia.util.BeanRestaurantDetails;
import com.app.coupondunia.util.CheckInternetConnection;
import com.app.coupondunia.util.CustomDialogLocation;
import com.app.coupondunia.util.ServiceGetAddress;

public class RestaurantListActivity extends ActionBarActivity implements
		LocationListener {
	protected LocationManager locationManager;
	Location currLoc = null; // current location
	CustomDialogLocation dialog; // dialog to show message for enabling location
									// services
	Intent startAddressService; // service intent for geo-coding
	TextView tvCurrLoc, tvErrMsg;
	Toolbar toolBar;
	AsyncGetRestaurantList restaurantListDownloader;
	List<BeanRestaurantDetails.Data> restaurantList = null;
	ProgressBar pbLoadRestaurantList;
	RecyclerView rvRestaurantList;
	String address = null;
	CheckInternetConnection chkInt;
	int storeResultCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// broadcast manager to receive address after geocoding is complete
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mMessageReceiver,
				new IntentFilter(AppConstants.ADDRESS_INTENT_FILTER));

		// all the views of layout
		tvCurrLoc = (TextView) findViewById(R.id.tvCurrLoc);
		tvErrMsg = (TextView) findViewById(R.id.tvErrMsg);
		toolBar = (Toolbar) findViewById(R.id.tool_bar);
		rvRestaurantList = (RecyclerView) findViewById(R.id.rvRestaurantList);
		rvRestaurantList.setLayoutManager(new LinearLayoutManager(this));
		pbLoadRestaurantList = (ProgressBar) findViewById(R.id.pbLoadRestaurantList);

		setSupportActionBar(toolBar);
		getSupportActionBar().setTitle(
				getResources().getString(R.string.activity_offer));

		// check internet connectivity
		chkInt = new CheckInternetConnection(this);
		if (chkInt.checkNow()) {
			restaurantListDownloader = new AsyncGetRestaurantList();
			restaurantListDownloader.SetListner(storeDownloadListner);
			restaurantListDownloader.execute("");
		} else {

			pbLoadRestaurantList.setVisibility(View.GONE);
			tvErrMsg.setText(getResources().getString(R.string.strNoConnection));
			tvErrMsg.setVisibility(View.VISIBLE);

		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		currLoc = location;
		// remove location updates
		locationManager.removeUpdates(this);
		// start service to get address
		startAddressService = new Intent(RestaurantListActivity.this,
				ServiceGetAddress.class);
		startAddressService.putExtra(AppConstants.LOCATION, currLoc);
		startService(startAddressService);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// check if location services are enabled
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& !locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

		{
			if (dialog != null)
				dialog.dismiss();

			tvCurrLoc.setVisibility(View.GONE);
			// show dialog to ask user turn on location
			ShowDialogLoc();
		} else if (currLoc == null && chkInt.checkNow()) {
			// only if current location has not been received , request updates
			// again
			tvCurrLoc.setVisibility(View.VISIBLE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
		} else if (!chkInt.checkNow()) {
			// when net connect
			tvCurrLoc.setVisibility(View.VISIBLE);
			tvCurrLoc.setText(getResources().getString(
					R.string.msgErrCurrentLocation));
		} else
			// normal case when screen resumed
			tvCurrLoc.setVisibility(View.VISIBLE);

	}

	// Show dialog for enabling location services
	public void ShowDialogLoc() {
		dialog = new CustomDialogLocation(this, getResources().getString(
				android.R.string.ok), getResources().getString(
				android.R.string.cancel), getResources().getString(
				R.string.msgEnable));
		dialog.SetPositiveBtnListner(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				Intent myIntent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				if (myIntent.resolveActivity(getPackageManager()) != null)
					startActivity(myIntent);
				dialog.dismiss();

			}
		});
		dialog.SetNegativeBtnListner(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				RestaurantListActivity.this.finish();
			}
		});
		dialog.setCancelable(false);
		dialog.show(getSupportFragmentManager(), "");
	}

	// Receiver to get the address
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				// Get extra data included in the Intent
				address = intent.getStringExtra(AppConstants.ADDRESS);
				if (address.equals("")) {
					if (currLoc != null)
						tvCurrLoc.setText(currLoc.getLatitude() + " , "
								+ currLoc.getLongitude());
					else
						tvCurrLoc.setText(getResources().getString(
								R.string.msgErrCurrentLocation));
				}

				else
					tvCurrLoc.setText(address);
				// unregister receiver
				LocalBroadcastManager.getInstance(RestaurantListActivity.this)
						.unregisterReceiver(mMessageReceiver);
				// check if store list has been downloaded successfully and
				// populate
				// data
				if (restaurantList != null
						&& storeResultCode == AppConstants.LIST_ITEMS_SUCCESS_CODE) {
					// set adapter to display data
					SortNDisplayData();
				}
			} catch (Exception e) {
				tvCurrLoc.setText(getResources().getString(
						R.string.msgErrCurrentLocation));
			}

		}
	};

	// Listener to get restaurant list after download with code
	RestaurantDownloadListner storeDownloadListner = new RestaurantDownloadListner() {

		@Override
		public void GetStoreList(
				List<BeanRestaurantDetails.Data> downloadedStoreList,
				int msgCode) {
			// TODO Auto-generated method stub

			restaurantList = downloadedStoreList;
			storeResultCode = msgCode;
			if (msgCode == AppConstants.LIST_ITEMS_SUCCESS_CODE) {

				if (address != null)
					SortNDisplayData();

			} else {
				// show error message
				pbLoadRestaurantList.setVisibility(View.GONE);
				tvErrMsg.setVisibility(View.VISIBLE);
			}

		}
	};

	// sort the restaurant list by distance and set the adapter of recycler view
	public void SortNDisplayData() {
		if (restaurantList.size() > 0) {
			Location locToComp;
			Float distance;
			for (int i = 0; i < restaurantList.size(); i++) {
				// calculate the distance
				locToComp = new Location("");
				locToComp.setLatitude(Double.parseDouble(restaurantList.get(i)
						.getLatitude()));
				locToComp.setLongitude(Double.parseDouble(restaurantList.get(i)
						.getLongitude()));
				distance = currLoc.distanceTo(locToComp);
				// set the distance
				restaurantList.get(i).setDistanceFromCurrLoc(distance);
			}

			// sort the list based on distance
			Collections.sort(restaurantList,
					new Comparator<BeanRestaurantDetails.Data>() {
						@Override
						public int compare(
								final BeanRestaurantDetails.Data object1,
								final BeanRestaurantDetails.Data object2) {
							return object1.getDistanceFromCurrLoc().compareTo(
									object2.getDistanceFromCurrLoc());
						}
					});

			// set the adapter
			String[] strAreaAddress = address.split(","); // for getting only
															// the last two
															// fragments of
															// address to be
			if (strAreaAddress.length >= 2) // passed in adapter
			{
				rvRestaurantList.setAdapter(new AdapterRestaurantDetail(
						restaurantList, RestaurantListActivity.this,
						strAreaAddress[strAreaAddress.length - 2]));
			} else
				rvRestaurantList.setAdapter(new AdapterRestaurantDetail(
						restaurantList, RestaurantListActivity.this, address));

			rvRestaurantList.setVisibility(View.VISIBLE);
		} else {
			// empty list message
			tvErrMsg.setText(getResources().getString(
					R.string.msgEmptyStoreList));
			tvErrMsg.setVisibility(View.GONE);
		}
		pbLoadRestaurantList.setVisibility(View.GONE);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// remove location updates
		locationManager.removeUpdates(this);
		restaurantList = null;

		// cancel async task if running
		if (restaurantListDownloader != null)
			if (restaurantListDownloader.getStatus() == Status.RUNNING)
				restaurantListDownloader.cancel(true);

		super.onDestroy();

	}
}
