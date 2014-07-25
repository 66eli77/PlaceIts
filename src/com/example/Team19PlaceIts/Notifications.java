package com.example.Team19PlaceIts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;

public class Notifications {
	
	private int NOTIFICATION_ID = 0;
	
	public void fireNotification(Context context, String title, String content){
		NotificationManager notificationManager = (NotificationManager) context.
				getSystemService(context.NOTIFICATION_SERVICE);
		 
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 
        		NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = createNotification();
        notification.setLatestEventInfo(context, "Place_Its: " + title, content, pendingIntent);
        notificationManager.notify(NOTIFICATION_ID, notification);
	}
	
	private Notification createNotification() {
		Notification notification = new Notification();
		notification.icon = R.drawable.map_logo;
		notification.when = System.currentTimeMillis();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = Color.WHITE;
		notification.ledOnMS = 1500;
		notification.ledOffMS = 1500;
		notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		return notification;
	}

}
