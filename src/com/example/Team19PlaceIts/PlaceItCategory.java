package com.example.Team19PlaceIts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;
import android.util.Log;

public class PlaceItCategory {
	private final String category;

	public PlaceItCategory(String name) {
		category = name;
	}

	@Override
	public boolean equals(Object o) {
		return category.equalsIgnoreCase(((PlaceItCategory) o).category);
	}

	@Override
	public String toString() {
		return category;
	}
	
}

/** A class, to download Google Places */
class PlacesTask extends AsyncTask<String, Integer, String>{

	String data = null;
	
	// Invoked by execute() method of this object
	@Override
	protected String doInBackground(String... url) {
		try{
			data = downloadUrl(url[0]);
		}catch(Exception e){
			 Log.d("Background Task",e.toString());
		}
		return data;
	}
	
	// Executed after the complete execution of doInBackground() method
	@Override
	protected void onPostExecute(String result){			
		ParserTask parserTask = new ParserTask();
		
		// Start parsing the Google places in JSON format
		// Invokes the "doInBackground()" method of the class ParseTask
		parserTask.execute(result);
	}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);                
                

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();                

                // Connecting to url 
                urlConnection.connect();                

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();
                
                System.out.println("eli " +  sb.toString());

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }

                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }

        return data;
    }

}



/** A class to parse the Google Places in JSON format */
class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

	JSONObject jObject;
	
	// Invoked by execute() method of this object
	@Override
	protected List<HashMap<String,String>> doInBackground(String... jsonData) {
	
		List<HashMap<String, String>> places = null;			
		PlaceJSONParser placeJsonParser = new PlaceJSONParser();
    
        try{
        	jObject = new JSONObject(jsonData[0]);
        	
            /** Getting the parsed data as a List construct */
            places = placeJsonParser.parse(jObject);
            
        }catch(Exception e){
                Log.d("Exception",e.toString());
        }
        return places;
	}
	
	// Executed after the complete execution of doInBackground() method
	@Override
	protected void onPostExecute(List<HashMap<String,String>> list){			
		
		// Clears all the existing markers 
		MainActivity.mMap.clear();
		
		for(int i=0;i<list.size();i++){
		
			// Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();
            
            // Getting a place from the places list
            HashMap<String, String> hmPlace = list.get(i);

            // Getting latitude of the place
            double lat = Double.parseDouble(hmPlace.get("lat"));	            
            
            // Getting longitude of the place
            double lng = Double.parseDouble(hmPlace.get("lng"));
            
            // Getting name
            String name = hmPlace.get("place_name");
            
            // Getting vicinity
            String vicinity = hmPlace.get("vicinity");
            
            LatLng latLng = new LatLng(lat, lng);
            
            // Setting the position for the marker
            markerOptions.position(latLng);

            // Setting the title for the marker. 
            //This will be displayed on taping the marker
            markerOptions.title(name + " : " + vicinity);	            

            // Placing a marker on the touched position
            MainActivity.mMap.addMarker(markerOptions);            
        
		}		
		
	}
	
}
