package com.example.Team19PlaceIts;

import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.model.Marker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

/*This is the Reciever for the Brodcast sent, here our app will be notified if the User is 
* in the region specified by our proximity alert.You will have to register the reciever 
* with the same Intent you broadcasted in the previous Java file 
*/

public class ProximityReceiver extends BroadcastReceiver{

	private ListMediator myListMediator = new ListMediator();
	List<Marker> mMarkers = myListMediator.getList();
	List<Marker> mCompletedMarkers = myListMediator.getCompletedList();
	Iterator marker = myListMediator.getMarkerIterator();
	private ProximityAlert mProximityAlert = new ProximityAlert();
	private Notifications mNotification = new Notifications();
	private WriteToFile myWrite = new WriteToFile();

	
	@Override
	public void onReceive(Context context, Intent intent) {
		  
	    String title = intent.getStringExtra("title");
	    String content = intent.getStringExtra("content");
	    String Id = intent.getStringExtra("Id");
		
		// Key for determining whether user is leaving or entering 
		String k = LocationManager.KEY_PROXIMITY_ENTERING;
		
		//Gives whether the user is entering or leaving in boolean form
		boolean entering = intent.getBooleanExtra(k, false);
		
		// INTO THE ZOOM
		if(entering){
			// Call the Notification Service or anything else that you would like to do here
			Toast.makeText(context, "Place_Its " + title + " is near you", 30).show();
System.out.println("my inZoom ID: " + Id );
			// Send notification when inside the zoom
			mNotification.fireNotification(context, title, content);
		}
		
		// OUT THE ZOOM
		else{
			//Other custom Notification 
			Toast.makeText(context, "Have you accomplished " + content + "?", 30).show();
			for (int i = 0; i < mMarkers.size(); i++) {
				Marker current = mMarkers.get(i);
				int requestCode = (int) (100000*current.getPosition().latitude
						+ 100000*current.getPosition().longitude);
				String m = "" + requestCode;
System.out.println("myRemove out ID:     " + m + " " + Id );
				if(m.equals(Id)){
					myListMediator.addToCompletedList(current);
					myListMediator.removeMarker(i);
					mProximityAlert.removeProximityAlert(context, requestCode);
					myWrite.writeActive(context);
System.out.println("myRemove in ID: " + m + " " + Id );
				}
			}
		}
	}


}
