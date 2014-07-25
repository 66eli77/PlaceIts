package com.example.Team19PlaceIts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddItemActivity extends Activity {
	public static final String TAG = "AddItemActivity";
	private Spinner spinner;
	private EditText item_name;
	private EditText item_desc;
	private EditText item_price;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> selectedItems;
	private Button registerItem;
	ListView listView;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		 registerItem = (Button) findViewById(R.id.register_item);
		
		
		String[] catList = getResources().getStringArray(R.array.place_type);
		//if (GooglePlusLoginActivity.user.getFlag())
		//	GooglePlusLoginActivity.user.initCat(catList);
		
		adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, catList);
		  listView = (ListView) findViewById(R.id.list);
		 listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        listView.setAdapter(adapter);
	        
	        /***** ************************************************************
	         * check marks on active
	         */
	        for (PlaceItCategory c: GooglePlusLoginActivity.user.traversCatagory()) {
	        	for (int i = 0; i < catList.length; i++) {
	        			System.out.println("yo "+c);
	        			if (catList[i].equals(c.toString()))
	        				listView.setItemChecked(i, true);
	        	}
	        }
	        /****************************************************************/
	        //  registerItem.setOnClickListener((OnClickListener) this);
	        registerItem.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					SparseBooleanArray checked = listView.getCheckedItemPositions();
				         selectedItems = new ArrayList<String>();
					         //check for previous items on server
					        //setItemChecked(pos, true);
					         
					         Log.v("delete", "passed");
					         String cat2 = "#";
					         
					         /*limit the selection*/
			if (listView.getCheckedItemCount() <=3) {
					        for (int i = 0; i < checked.size(); i++) {
					            // Item position in adapter
					            int position = checked.keyAt(i);
					            System.out.println("checked "+Integer.toString(position));
					            // Add item if it is checked i.e.) == TRUE!
					            if (checked.valueAt(i))
					            	cat2 += adapter.getItem(position)+"#";
					                //GooglePlusLoginActivity.user.addCateogry(new PlaceItCategory(adapter.getItem(position)));
					            //else   GooglePlusLoginActivity.user.removeCategory(new PlaceItCategory(adapter.getItem(checked.keyAt(i))));
					        }
					        GooglePlusLoginActivity.user.setCategoryFromServer(cat2);
					        System.out.println("set from server "+ cat2);
							 
					        String[] outputStrArr = new String[selectedItems.size()];
					 
					        for (int i = 0; i < selectedItems.size(); i++) {
					            outputStrArr[i] = selectedItems.get(i);
					        }
					 
					       /* *Code if you want to export the selected items to new intent
					        * 
					        * Intent intent = new Intent(getApplicationContext(),
					                ResultActivity.class);
					 
					        // Create a bundle object
					        Bundle b = new Bundle();
					        b.putStringArray("selectedItems", outputStrArr);
					 
					        // Add the bundle to the intent.
					        intent.putExtras(b);
					 
					        // start the ResultActivity
					        startActivity(intent); */
					    //    postdata();
					       String[] cats = cat2.split("#");
					       int size = 0;
					       for(int i = 0; i < cats.length; i++){
					    	   if(cats[i].length() > 0) size++;
					       }
					      /*  if (size <= 3) {
					        	postdata();
					        	Intent myIntent = new Intent(AddItemActivity.this, MainActivity.class);
					        	startActivity(myIntent);
					        	}
					      */ 
					       postdata();
					 //     loadMarkersThreads task = new loadMarkersThreads();
					       
					   //           task.execute(new String[] { "demo" });

					      Intent myIntent = new Intent(AddItemActivity.this, MainActivity.class);
				        	startActivity(myIntent);
					      
			}
					        else  {
					        	Toast.makeText(AddItemActivity.this, "Why you don't follow direction, pick 3, :)", 
										Toast.LENGTH_SHORT).show();
					        }
									
							
				}
					
				});
	        
	        
		/*registerItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				postdata();
				Intent myIntent = new Intent(AddItemActivity.this, MainActivity.class);
				startActivity(myIntent);
			}
		});*/
	   
		//dialog = ProgressDialog.show(this, "Loading Catogory Markers data...", "Please wait...", false);
	    
		//dialog.show();
	//	new UpdateSpinnerTask().execute(MainActivity.PRODUCT_URI);

	}

	/*
	 private void findViewsById() {
	        listView = (ListView) findViewById(R.id.list);
	        registerItem = (Button) findViewById(R.id.register_item);
	    }
	 
	 */
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void postdata() {
		final ProgressDialog dialog = ProgressDialog.show(this,
				"Posting Data...", "Please wait...", false);
		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(MainActivity.ITEM_URI);
				
			    try {
			    	//for (Iterator<String> iterator = selectedItems.iterator(); iterator
					//		.hasNext();) {
					//	String s = iterator.next();
			    	//for (PlaceItCategory c : GooglePlusLoginActivity.user.traversCatagory()) {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
						  nameValuePairs.add(new BasicNameValuePair("name",
								  "c"+GooglePlusLoginActivity.user.getId()));
						  nameValuePairs.add(new BasicNameValuePair("description",
								  "category"));
						  nameValuePairs.add(new BasicNameValuePair("price",
								  GooglePlusLoginActivity.user.getStrToUpload()));
						  nameValuePairs.add(new BasicNameValuePair("product",
								  GooglePlusLoginActivity.user.getId()));
						 nameValuePairs.add(new BasicNameValuePair("action",
						       "put"));
						  
						/*  ////adding marker to server
						  System.out.println(GooglePlusLoginActivity.user.getMarkerToUpload());
						  nameValuePairs.add(new BasicNameValuePair("name",
								  "m"+GooglePlusLoginActivity.user.getId()));
						  nameValuePairs.add(new BasicNameValuePair("description",
								  "marker"));
						  nameValuePairs.add(new BasicNameValuePair("price",
								  GooglePlusLoginActivity.user.getMarkerToUpload()));
						  nameValuePairs.add(new BasicNameValuePair("product",
								  GooglePlusLoginActivity.user.getId()));
						  nameValuePairs.add(new BasicNameValuePair("action",
						          "put"));
						  
						  */
						  
						  post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

						  HttpResponse response = client.execute(post);
  
						  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						  String line = "";
						  while ((line = rd.readLine()) != null) {
							  Log.d(TAG, line);
						  }
			    //	}

			    } catch (IOException e) {
			    	Log.d(TAG, "IOException while trying to conect to GAE");
			    }
				dialog.dismiss();
			}
		};

		t.start();
		dialog.show();
	}

private class loadMarkersThreads extends AsyncTask<String, Void, String> {
	protected String doInBackground(String... urls) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(MainActivity.ITEM_URI);
		
	    try {
	    	//for (Iterator<String> iterator = selectedItems.iterator(); iterator
			//		.hasNext();) {
			//	String s = iterator.next();
	    	//for (PlaceItCategory c : GooglePlusLoginActivity.user.traversCatagory()) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
				  
				  
				  ////adding marker to server
				  nameValuePairs.add(new BasicNameValuePair("name",
						  "m"+GooglePlusLoginActivity.user.getId()));
				  nameValuePairs.add(new BasicNameValuePair("description",
						  "marker"));
				  nameValuePairs.add(new BasicNameValuePair("price",
						  GooglePlusLoginActivity.user.getMarkerToUpload()));
				  nameValuePairs.add(new BasicNameValuePair("product",
						  GooglePlusLoginActivity.user.getId()));
				  nameValuePairs.add(new BasicNameValuePair("action",
				          "put"));
				  
				  
				  
				  post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				  HttpResponse response = client.execute(post);

				  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				  String line = "";
				  while ((line = rd.readLine()) != null) {
					  Log.d(TAG, line);
				  }
	    //	}

	    } catch (IOException e) {
	    	Log.d(TAG, "IOException while trying to conect to GAE");
	    }
	    return null;
	}
	 
	@Override        
	protected void onPostExecute(String result) {
		/**PUSHES categories to the sever
		 * worst case call it in the upper function with the activitiy  
		 */
		postdata(); 
	}

	 
}
	 
	 private void postdataMarker() {
			final ProgressDialog dialog = ProgressDialog.show(this,
					"Posting Data...", "Please wait...", false);
			Thread t = new Thread() {

				public void run() {
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(MainActivity.ITEM_URI);
					
				    try {
				    	//for (Iterator<String> iterator = selectedItems.iterator(); iterator
						//		.hasNext();) {
						//	String s = iterator.next();
				    	//for (PlaceItCategory c : GooglePlusLoginActivity.user.traversCatagory()) {
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
							  
							  
							  ////adding marker to server
							  nameValuePairs.add(new BasicNameValuePair("name",
									  "marker"));
							  nameValuePairs.add(new BasicNameValuePair("description",
									  "marker"));
							  nameValuePairs.add(new BasicNameValuePair("price",
									  GooglePlusLoginActivity.user.getMarkerToUpload()));
							  nameValuePairs.add(new BasicNameValuePair("product",
									  GooglePlusLoginActivity.user.getId()));
							  nameValuePairs.add(new BasicNameValuePair("action",
							          "put"));
							  
							  
							  
							  post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

							  HttpResponse response = client.execute(post);
	  
							  BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							  String line = "";
							  while ((line = rd.readLine()) != null) {
								  Log.d(TAG, line);
							  }
				    //	}

				    } catch (IOException e) {
				    	Log.d(TAG, "IOException while trying to conect to GAE");
				    }
					dialog.dismiss();
				}
			};
			t.start();
			dialog.show();
		}
}
