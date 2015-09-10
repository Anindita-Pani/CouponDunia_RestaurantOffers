package com.app.coupondunia.util;

/* Object mapper for json data */
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class BeanRestaurantDetails {
	Status status;
	Map<String, Data> data;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Map<String, Data> getData() {
		return data;
	}

	public void setData(Map<String, Data> data) {
		this.data = data;
	}

	public class Data {

		Float distanceFromCurrLoc;

		public Float getDistanceFromCurrLoc() {
			return distanceFromCurrLoc;
		}

		public void setDistanceFromCurrLoc(Float distanceFromCurrLoc) {
			this.distanceFromCurrLoc = distanceFromCurrLoc;
		}

		@SerializedName("OutletName")
		String OutletName;
		@SerializedName("Latitude")
		String Latitude;
		@SerializedName("Longitude")
		String Longitude;
		@SerializedName("NumCoupons")
		String NumCoupons;
		@SerializedName("LogoURL")
		String LogoURL;

		public String getLogoURL() {
			return LogoURL;
		}

		public void setLogoURL(String logoURL) {
			LogoURL = logoURL;
		}

		public String getNumCoupons() {
			return NumCoupons;
		}

		public void setNumCoupons(String numCoupons) {
			NumCoupons = numCoupons;
		}

		List<Categories> Categories;

		public String getLatitude() {
			return Latitude;
		}

		public void setLatitude(String latitude) {
			Latitude = latitude;
		}

		public String getLongitude() {
			return Longitude;
		}

		public void setLongitude(String longitude) {
			Longitude = longitude;
		}

		public List<Categories> getCategories() {
			return Categories;
		}

		public void setCategories(List<Categories> categories) {
			this.Categories = categories;
		}

		public String getOutletName() {
			return OutletName;
		}

		public void setOutletName(String outletName) {
			OutletName = outletName;
		}

		public class Categories {

			@SerializedName("Name")
			String Name;

			public String getName() {
				return Name;
			}

			public void setName(String name) {
				Name = name;
			}

		}

	}

	class Status {
		@SerializedName("rcode")
		String rcode;
		@SerializedName("message")
		String message;

		public String getRcode() {
			return rcode;
		}

		public void setRcode(String rcode) {
			this.rcode = rcode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}
}
