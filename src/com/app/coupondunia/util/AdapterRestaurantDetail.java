package com.app.coupondunia.util;

/* Adapter for the recycler view to display restaurant list */
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.coupondunia.R;
import com.app.coupondunia.util.AdapterRestaurantDetail.RestaurantViewHolder;
import com.squareup.picasso.Picasso;

public class AdapterRestaurantDetail extends RecyclerView.Adapter<RestaurantViewHolder> {
	private List<BeanRestaurantDetails.Data> storeList;
	private Context context;
	RestaurantViewHolder holder;
	String address;

	public AdapterRestaurantDetail(List<BeanRestaurantDetails.Data> storeList,
			Context context, String address) {

		this.context = context;
		this.storeList = storeList;
		this.address = address;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return storeList.size();
	}

	@Override
	public void onBindViewHolder(RestaurantViewHolder holder, int i) {
		// TODO Auto-generated method stub
		BeanRestaurantDetails.Data storeObj = storeList.get(i);
		StringBuilder strCategory = new StringBuilder(); // for category
		holder.tvStoreName.setText(storeObj.getOutletName()); // set outlet name
		holder.tvNoOfOffers.setText(context.getResources().getQuantityString(
				R.plurals.offers, Integer.parseInt(storeObj.getNumCoupons()),
				storeObj.getNumCoupons())); // set number of offers

		holder.tvDistance.setText("" + storeObj.getDistanceFromCurrLoc()
				+ " m " + address); // set distance
		Picasso.with(context).load(storeObj.getLogoURL())
				.into(holder.imgStoreLogo); // load image
		
		// get category names and concat them
		for (int counter = 0; counter < storeObj.getCategories().size(); counter++) {
			strCategory.append( " <big><b>&bull;</b></big>  "+storeObj.getCategories().get(counter).getName());
		}
		holder.tvCategory.setText(Html.fromHtml(strCategory.toString())); // set the category
	}

	@Override
	public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		// TODO Auto-generated method stub

		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.list_item_place_dtl, null);
		RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT);
		v.setLayoutParams(lp);
		holder = new RestaurantViewHolder(v);

		return holder;
	}

	// View holder for list items
	public class RestaurantViewHolder extends RecyclerView.ViewHolder {
		TextView tvStoreName, tvNoOfOffers, tvCategory, tvDistance;
		ImageView imgStoreLogo;

		public RestaurantViewHolder(View itemView) {
			super(itemView);
			tvStoreName = (TextView) itemView.findViewById(R.id.tvStoreName);
			tvNoOfOffers = (TextView) itemView.findViewById(R.id.tvNumOfOffers);
			tvCategory = (TextView) itemView.findViewById(R.id.tvCategories);
			tvDistance = (TextView) itemView
					.findViewById(R.id.tvDistFromCurrLoc);
			imgStoreLogo = (ImageView) itemView.findViewById(R.id.imgStoreLogo);

		}

	}

}
