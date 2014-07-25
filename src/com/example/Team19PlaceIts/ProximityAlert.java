package com.example.Team19PlaceIts;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class ProximityAlert {
	
	float radius = 50;   // this is 50m radius
	
	public void AddProximityAlert(Marker added, Context context){
		
		LatLng point = added.getPosition();
		LocationManager lm = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		
		// use the latitude and longitude to generate the unique code for PendingIntent
		int requestCode = (int) (100000*added.getPosition().latitude
				+ 100000*added.getPosition().longitude);
		String Id = "" + requestCode;
		
		Intent i = new Intent("com.example.Team19PlaceIts.ProximityAlert");
		String title = added.getTitle();
		String content = added.getSnippet();
		i.putExtra("title", title.toString());
		i.putExtra("content", content.toString());
		i.putExtra("Id", Id);
		
		//Custom Action
//		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
//				i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				i, PendingIntent.FLAG_UPDATE_CURRENT);
//		lm.requestLocationUpdates(
	//			LocationManager.GPS_PROVIDER,
		//		MINIMUM_TIME_BETWEEN_UPDATES,
			//	MINIMUM_DISTANCE_CHANGE_FOR_UPDATES);
		lm.addProximityAlert(point.latitude, point.longitude, radius, -1, pi);
	}
	
	public void removeProximityAlert(Context context, int requestCode) {
		
		// int requestCode is the unique identifier for each Intent
		// here I convert marker's ID to int, the ID is something like "m0" or "m1"
		//int requestCode = 0;
		//String markerID = deleted.getId();
		//for (char c : markerID.toCharArray()){
		//	requestCode = requestCode + ((int)c);
		//}
System.out.println("my removeProximityAlert ID: " + requestCode);
		LocationManager locationManager = 
				(LocationManager)context.getSystemService(context.LOCATION_SERVICE);

		Intent i = new Intent("com.example.Team19PlaceIts.ProximityAlert"); 
		PendingIntent tobeDelete = PendingIntent.getBroadcast(context, requestCode ,
				i, PendingIntent.FLAG_UPDATE_CURRENT);
		locationManager.removeProximityAlert(tobeDelete);
		tobeDelete.cancel();
	}
}
