package com.example.Team19PlaceIts;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.os.Environment;

public class WriteToFile {
	
	private ListMediator myListMediator = new ListMediator();
	private List<Marker> mMarkers = myListMediator.getList();
	private Iterator marker = myListMediator.getMarkerIterator();
	private List<Marker> mCompletedMarkers = myListMediator.getCompletedList();
	private Iterator Completedmarker = myListMediator.getCompletedMarkerIterator();
	
	public void writeCompleted(Context context){
		// write file to save marker info
				if (isExternalStorageWritable()) {
					try {
						// File myRecord = getAlbumStorageDir("myRecord");
						File myRecord = new File(context.getExternalFilesDir(null), "completedList");
						FileOutputStream fos2 = new FileOutputStream(myRecord);
						String string;
						// reset the iterator to the beginning
						Completedmarker = mCompletedMarkers.iterator();
						while (Completedmarker.hasNext()) {
							Marker current = (Marker) Completedmarker.next();
							LatLng point = current.getPosition();
							string = point.latitude + "º" + point.longitude + "º"
									+ current.getTitle() + "º" + current.getSnippet()
									+ "º"; // º = alt + 'b'
							fos2.write(string.getBytes());
						}
						fos2.close();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
	}
	
	public void writeActive(Context context){
		// write file to save marker info
				if (isExternalStorageWritable()) {
					try {
						// File myRecord = getAlbumStorageDir("myRecord");
						File myRecord = new File(context.getExternalFilesDir(null), "activeList");
						FileOutputStream fos2 = new FileOutputStream(myRecord);
						String string;
						// reset the iterator to the beginning
						marker = mMarkers.iterator();
						while (marker.hasNext()) {
							Marker current = (Marker) marker.next();
							LatLng point = current.getPosition();
							string = point.latitude + "º" + point.longitude + "º"
									+ current.getTitle() + "º" + current.getSnippet()
									+ "º"; // º = alt + 'b'
							System.out.println("Write Eli ! ! ! : " + string
									+ current.getId());
							fos2.write(string.getBytes());
						}
						fos2.close();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
}
