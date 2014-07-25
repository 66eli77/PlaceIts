package com.example.Team19PlaceIts;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

/*
 * 
 * 
 * 
 * 
 * NOT IN USE RIGHT NOW!!!!!!!!!!!!!!!!!
 * 
 * 
 * 
 * display new layout as a part of new activity which will be started 
 * when user will click any of the notifications
 */

public class OnClickActivity  extends Activity{
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_onclick);
   }

   // Run method if user press Close Button in the 
   // notification pop-up window 
   public void closePostIT(View view) {
	  //add to completed list
	  //close the window?
	   
	   //launch the MainActivitiy 
	   Intent i = new Intent(this, MainActivity.class);
	   startActivity(i);
	   
	   
   }
   
   // Run method if user press Discard  Button in the 
   // notification pop-up window 
   public void discardPostIT(View view) {
	   
   }   
   
   // Run method if user press Repost in the 
   // notification pop-up window 
   public void repostPostIT(View view) {
	   
   }
}