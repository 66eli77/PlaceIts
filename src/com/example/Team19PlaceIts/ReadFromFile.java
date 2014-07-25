package com.example.Team19PlaceIts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.d;

import android.content.Context;
import android.os.Environment;

public class ReadFromFile {
	
	private ListMediator myListMediator = new ListMediator();
	private List<Marker> mMarkers = myListMediator.getList();
	private Iterator marker = myListMediator.getMarkerIterator();
	private List<Marker> mCompletedMarkers = myListMediator.getCompletedList();
	private Iterator Completedmarker = myListMediator.getCompletedMarkerIterator();
	
	public void readCompleted(Context context, GoogleMap mMap){
		// read the file to retrieve markers
				if (isExternalStorageReadable()) {
					try {
						File myRecord = new File(context.getExternalFilesDir(null), "completedList");
						BufferedReader inputReader = new BufferedReader(
								new InputStreamReader(new FileInputStream(myRecord)));
						String inputString;
						StringBuffer stringBuffer = new StringBuffer();
						while ((inputString = inputReader.readLine()) != null) {
							stringBuffer.append(inputString + "\n");
						}
						String[] parts = stringBuffer.toString().split("�");
						for (int i = 0; i < parts.length - 1; i = i + 4) {
							LatLng pos = new LatLng(Double.parseDouble(parts[i]),
									Double.parseDouble(parts[i + 1]));
							Marker added = mMap.addMarker(new MarkerOptions().position(pos)
									.title(parts[i + 2]).snippet(parts[i + 3]));	
							// populate the list after the earlier clearing
							mCompletedMarkers.add(added);
						}
						inputReader.close();
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
	}
	
	public void readActive(Context context, GoogleMap mMap){
		// read the file to retrieve markers
				if (isExternalStorageReadable()) {
					try {
						File myRecord = new File(context.getExternalFilesDir(null), "activeList");
						BufferedReader inputReader = new BufferedReader(
								new InputStreamReader(new FileInputStream(myRecord)));
						String inputString;
						StringBuffer stringBuffer = new StringBuffer();
						while ((inputString = inputReader.readLine()) != null) {
							stringBuffer.append(inputString + "\n");
						}
		System.out.println("fuck yeah: " + stringBuffer.toString());
						String[] parts = stringBuffer.toString().split("�");
						for (int i = 0; i < parts.length - 1; i = i + 4) {
							LatLng pos = new LatLng(Double.parseDouble(parts[i]),
									Double.parseDouble(parts[i + 1]));
		System.out.println("Read Eli ! ! ! : ");
							Marker added = mMap.addMarker(new MarkerOptions().position(pos)
									.title(parts[i + 2]).snippet(parts[i + 3]));
							
							// populate the list after the earlier clearing
							mMarkers.add(added);
						}
						inputReader.close();
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
	
}
