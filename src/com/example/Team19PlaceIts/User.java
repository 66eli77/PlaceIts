package com.example.Team19PlaceIts;

/*
	 * To change this license header, choose License Headers in Project Properties.
	 * To change this template file, choose Tools | Templates
	 * and open the template in the editor.
	 */

	import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

	import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

	import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

	import com.example.Team19PlaceIts.PlaceItMarker;
import com.example.Team19PlaceIts.PlaceItCategory;
import com.google.android.gms.maps.model.Marker;
	//import com.example.gaedemo.MainActivity;
	//import com.example.gaedemo.ShowItemsActivity.UpdateSpinnerTask;

	/**
	 * 
	 * @author mustafa.. Nooo! i code , he watch
	 */

	public class User {
		public static final String TAG = "ShowItemActivity";


		private final String emailAddress;
		private final String firstName;
		private final String lastName;
		private String id;
		private List<PlaceItMarker> markerActive;
		private List<PlaceItMarker> markerComplete;
		private List<PlaceItCategory> category;
		private List<PlaceItCategory> categoryTracker;

		public User(String em, String first, String last) {
			this.emailAddress = em;
			this.firstName = first;
			this.lastName = last;
			this.category = new java.util.ArrayList<PlaceItCategory>();
			this.markerActive = new java.util.ArrayList<PlaceItMarker>();
			this.categoryTracker = new java.util.ArrayList<PlaceItCategory>();
			this.markerComplete = new java.util.ArrayList<PlaceItMarker>();
		}

		public void addCateogry(PlaceItCategory c) {
			if(!this.categoryTracker.contains(c))
				this.categoryTracker.add(c);
			if(!this.category.contains(c))
				this.category.add(c);
		}
		public String getFirstName() {
			return this.firstName;
		}
//		public void addMarker(PlaceItMarker m) {
//			if(!this.markerTracker.contains(m))
//				this.markerTracker.add(m);
//			if(!this.marker.contains(m))
//				this.marker.add(m);
//		}

		public boolean removeCategory(PlaceItCategory c) {
			this.categoryTracker.remove(c);
			return this.category.remove(c);
		}

//		public boolean removeMarker(PlaceItMarker m) {
//			this.markerTracker.remove(m);
//			return this.marker.remove(m);
//		}

		public Iterable<PlaceItCategory> traversCatagory() {
			return this.category;
		}

//		public Iterable<PlaceItMarker> traversMarker() {
//			return this.marker;
//		}

		public String getStrToUpload() {
			// get category, all info from category list is is returned
			this.categoryTracker.clear(); // reset tracker, all send to updated
			String cat = "#";
			for (PlaceItCategory c : this.category) {
				cat += c + "#";
			}
			return cat;
		}

		public String getMarkerToUpload() {
			String mark = "#";
			for (PlaceItMarker m : this.markerActive) {
				mark += m + "#";
			}
			for( PlaceItMarker m : this.markerComplete){
				mark += m + "";
			}
			System.out.println("marker being added: "+mark);
			return mark;
		}

		public void setMarkerFromServer(String ms) {
			if(ms.length() == 0) return; 
			String[] markers = ms.split("#");
			// empty data base
			this.markerActive.clear();
			this.markerComplete.clear();
			for (int i = 0; i < markers.length; i++) {
				if (markers[i].length() == 0)
					continue;
				PlaceItMarker pm = new PlaceItMarker(markers[i]);
				if(pm.status()) 
					this.markerActive.add(pm);
				else 
					this.markerComplete.add(pm);
			}
			
		}
		
		

		public void setCategoryFromServer(String cat) {
			if(cat.length() <= 0) return;
			this.category.clear();
			String[] c = cat.split("#");
			for (int i = 0; i < c.length; i++) {
				if (c[i].length() == 0)
					continue;
				this.category.add(new PlaceItCategory(c[i]));
			}
//			if (this.categoryTracker.size() != 0) {
	//			for (PlaceItCategory t : this.categoryTracker) {
		//			this.category.add(t);
			//	}
//			}
		}
		


		public String getId() {
			return this.id;
		}

		public void setId(String id) {
			this.id = id;
		}
		//read to server
		public void syncActiveFromUser(List<Marker> m){
			for(PlaceItMarker k : markerActive){
				m.add(k.getMarker());
			}
		}
		//write to server
		public void syncActiveToUser(List<Marker> m){
			this.markerActive.clear();
			for(Marker k : m){
				PlaceItMarker pm = new PlaceItMarker(k);
				pm.on();
				this.markerActive.add(pm);
			}
		}
		
		public void syncCompFromUser(List<Marker> m){
			for(PlaceItMarker k : markerComplete){
				m.add(k.getMarker());
			}
		}
		public void syncCompToUser(List<Marker> m){
			this.markerActive.clear();
			for(Marker k : m){
				PlaceItMarker pm = new PlaceItMarker(k);
				pm.off();
				this.markerComplete.add(pm);
			}
		}

		// public List<String> list = new ArrayList<String>();
		public String demo;
		public List<String> getList;

		// Log.v("list", list.get(0));
		public void getData() {
			new UpdateSpinnerTask().execute(MainActivity.ITEM_URI);
			// Log.v("list", demo);

			// Log.v("list", ng.get(0));
			// return ng.get(0);

		}
		public String getPlaces() {
			String p = "";
			for (PlaceItCategory c: this.category){
				p += c.toString() + "|";
			}
			if(p.length() > 1){
				return p.substring(0, p.length() - 1);
			}
			return p;
			
		}
		private class UpdateSpinnerTask extends
				AsyncTask<String, Void, List<String>> {

			List<String> list = new ArrayList<String>();
		//	String cat;
			@Override
			protected List<String> doInBackground(String... url) {
//			protected String doInBackground(String... url) {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url[0]);
				try {
					HttpResponse response = client.execute(request);
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);
					System.out.println("data" + data);
					Log.d(TAG, data);
					JSONObject myjson;

					try {
						myjson = new JSONObject(data);
						JSONArray array = myjson.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							// User.this.addCateogry(new
							String n = (obj.get("name").toString()).substring(0,1)+ ": ";
							list.add(n + obj.get("price").toString());
							 System.out.println("List items:"+list.get(i));

						}

					} catch (JSONException e) {

						Log.d(TAG, "Error in parsing JSON");
					}

				} catch (ClientProtocolException e) {

					Log.d(TAG,
							"ClientProtocolException while trying to connect to GAE");
				} catch (IOException e) {

					Log.d(TAG, "IOException while trying to connect to GAE");
				}
				return list;
			}

			protected void onPostExecute(List<String> list) {
				getList = list;
				//for (String s : getList) {
				//	User.this.addCateogry(new PlaceItCategory(s));
				//}
				System.out.println("list size"+ getList.size());
				if(getList.size() != 0){
					if((getList.get(0).substring(0, 1).equals("c") ))
						User.this.setCategoryFromServer(getList.remove(0).substring(3));
					else 
						User.this.setMarkerFromServer(getList.remove(0).substring(3));
				}
				if(getList.size() != 0){
					if((getList.get(0).substring(0, 1).equals("m") ))
						User.this.setMarkerFromServer(getList.remove(0).substring(3));
					else 
						User.this.setCategoryFromServer(getList.remove(0).substring(3));
				}
				// dialog.dismiss();

			}

		}

	}