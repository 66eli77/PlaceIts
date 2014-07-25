package com.example.Team19PlaceIts;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.example.Team19PlaceIts.ListMediator;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CompletedListLoader extends ListActivity {

	private ListMediator myListMediator = new ListMediator();
	ArrayAdapter<String> adapter;
	private List<Marker> completedMarkers = myListMediator.getCompletedList();
	private List<Marker> mMarkers = myListMediator.getList();
	private Iterator Completedmarker = myListMediator.getCompletedMarkerIterator();
	private WriteToFile myWrite = new WriteToFile();
	private ReadFromFile myRead = new ReadFromFile();
	private ProximityAlert mProximityAlert = new ProximityAlert();
	int Num = 0;     // this Num will increment to set unique key for Intent
	Intent data = new Intent();   //this intent will be used to send mPosition when repost is pressed
	
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	    
	
	    List<String> mList = new ArrayList<String>();
	    while (Completedmarker.hasNext()) {
			Marker current = (Marker) Completedmarker.next();
			mList.add(current.getTitle());
		}
	    adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, mList);
	    setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		final int mPosition = position;
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Completed list management");
//		alert.setMessage("Please edit your reminder:");
		alert.setPositiveButton("repost", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// add to the active list
				mMarkers.add(completedMarkers.get(mPosition));
				
				//delete from the completed list
				String tobeDelete = completedMarkers.get(mPosition).getTitle();
				adapter.remove(tobeDelete);
				
			//	myListMediator.removeCompletedMarker(mPosition);
				
				// Prepare data intent 
				data.putExtra("Key" + Num, mPosition);
				Num = Num + 1;
				setResult(RESULT_OK, data);
			}
		});
		
		alert.setNegativeButton("delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String tobeDelete = completedMarkers.get(mPosition).getTitle();
						adapter.remove(tobeDelete);
						myListMediator.removeCompletedMarker(mPosition);
					}
				});

		alert.show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// read the file to retrieve markers
	//	myRead.readCompleted(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// write file to save completed marker info
		//myWrite.writeCompleted(this);
		myWrite.writeActive(this);
	}
	
}
