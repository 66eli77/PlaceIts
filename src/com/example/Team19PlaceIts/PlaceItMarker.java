package com.example.Team19PlaceIts;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class PlaceItMarker {

	/**
	 * @param args
	 */
	private final String title;
	private final String desc;
	private final String latitude;
	private final String longitude;
	LatLng lt;
	private boolean active;
	Marker m;

	public PlaceItMarker(String t, String d, String x, String y, boolean b) {
		title = t;
		desc = d;
		this.latitude = x;
		this.longitude = y;
		lt = new LatLng(Double.parseDouble(x), Double.parseDouble(y));
		active = b;
	}

	public PlaceItMarker(Marker m) {
		this.title = m.getTitle();
		this.desc = m.getSnippet();
		this.lt = m.getPosition();
		this.latitude = lt.latitude + "";
		this.longitude = lt.longitude + "";
		this.m = m;
	}

	public PlaceItMarker(String str) {
		String[] data = str.split("%");
		title = data[0];
		desc = data[1];
		this.latitude = data[2];
		this.longitude = data[3];
		lt = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
		this.active = data[4].equals("true");

	}

	public LatLng getLatLng() {
		return lt;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDesc() {
		return this.desc;
	}
	
	public boolean status(){
		return this.active;
	}
	
	public void on(){
		this.active = true;
	}
	
	public void off(){
		this.active = false;
	}

	public Marker getMarker(){
		return this.m;
	}
	public boolean equals(Object obj) {
		PlaceItMarker that = (PlaceItMarker) obj;
		return this.latitude.equals(that.latitude) &&
				this.longitude.equals(that.longitude);
	}

	public String toString() {
		return this.title + "%" + this.desc + "%" + this.latitude + "%"
				+ this.longitude + "%"+active+"%";
	}

}