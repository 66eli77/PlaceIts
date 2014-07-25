package com.example.Team19PlaceIts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.model.Marker;
import com.example.Team19PlaceIts.ListMediator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ActiveListLoader extends ListActivity {
	
	private ListMediator myListMediator = new ListMediator();
	ArrayAdapter<String> adapter;
	List<Marker> mMarkers = myListMediator.getList();
	Iterator marker = myListMediator.getMarkerIterator();
	private WriteToFile myWrite = new WriteToFile();
	final String activeList = "activeList";  //file name for active list 
	int Num = 0;     // this Num will increment to set unique key for Intent
	Intent data = new Intent();   //this intent will be used to send requestCode when delete is pressed
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	    
	
	    List<String> mList = new ArrayList<String>();
	    while (marker.hasNext()) {
			Marker current = (Marker) marker.next();
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
	    LayoutInflater inflater = this.getLayoutInflater();
		alert.setTitle("Active list management");
		alert.setMessage("Please edit your reminder:");
		final View myInflatorView = inflater.inflate(R.layout.dialog_signin, null);
		alert.setView(myInflatorView);
		final EditText titleInput = (EditText) myInflatorView
				.findViewById(R.id.title);		
		final EditText contentInput = (EditText) myInflatorView
				.findViewById(R.id.content);
		
		// display content's original title and content
		final Marker current = mMarkers.get(mPosition);
		final String titleValue = current.getTitle();
		String contentValue = current.getSnippet();
		titleInput.setText(titleValue);
		contentInput.setText(contentValue);
		
		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//retrieve input from EditText
				final EditText titleInput = (EditText) myInflatorView
						.findViewById(R.id.title);
				final EditText contentInput = (EditText) myInflatorView
						.findViewById(R.id.content);
				String addedTitle = titleInput.getText().toString();
				String addedContent = contentInput.getText().toString();
				current.setTitle(addedTitle);
				current.setSnippet(addedContent);
				
				//update list element's name
				adapter.remove(titleValue);
				adapter.insert(addedTitle, mPosition);
			}
		});
		
		alert.setNegativeButton("delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
System.out.println("my position: " + mPosition + " list size: " + mMarkers.size());		
						String tobeDelete = mMarkers.get(mPosition).getTitle();
						adapter.remove(tobeDelete);
						Marker deleted = mMarkers.get(mPosition);
						myListMediator.removeMarker(mPosition);
						int requestCode = (int) (100000*deleted.getPosition().latitude
								+ 100000*deleted.getPosition().longitude);
						
						// Prepare data intent 
						data.putExtra("Key" + Num, requestCode);
						Num = Num + 1;
						setResult(RESULT_OK, data);
					}
				}
		);

		alert.show();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// write file to save marker info
		myWrite.writeActive(this);
	}
	
}