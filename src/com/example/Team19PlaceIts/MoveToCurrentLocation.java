package com.example.Team19PlaceIts;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MoveToCurrentLocation {

	public void moveToCurrentLocation(Context context, GoogleMap mMap, int zoomLevel) {
		// move map to current location
		LocationManager mLocationManager = (LocationManager) context
				.getSystemService(context.LOCATION_SERVICE);
		Location location = null;
		String[] providers = new String[3];
		providers[0] = LocationManager.GPS_PROVIDER;
		providers[1] = LocationManager.NETWORK_PROVIDER;
		providers[2] = LocationManager.PASSIVE_PROVIDER;

		for (String provider : providers) {
			if (!mLocationManager.isProviderEnabled(provider))
				continue;
			// mLocationManager.requestLocationUpdates(provider,
			// MIN_TIME_BW_UPDATES, MIN_DISTANCE_UPDATES, this);
			if (mLocationManager != null) {
				location = mLocationManager.getLastKnownLocation(provider);
				if (location != null) {
					break;
				}
			}
		}
		// this is the default value in case it cannot get the current location
		LatLng current = new LatLng(32.86, -117.23);

		Double lat, lon;
		try {
			lat = location.getLatitude();
			lon = location.getLongitude();
			current = new LatLng(lat, lon);
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(current) // Sets the center of the map to Mountain
										// View
					.zoom(zoomLevel) // Sets the zoom
					.bearing(0) // Sets the orientation of the camera to east
					.tilt(0) // Sets the tilt of the camera to 30 degrees
					.build();
			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		} catch (NullPointerException e) {
			mMap.animateCamera(CameraUpdateFactory.newLatLng(current), 2000,
					(CancelableCallback) context);
		}

	}
}
