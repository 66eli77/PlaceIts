package com.example.Team19PlaceIts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.model.Marker;

public class ListMediator {
	
	private static List<Marker> mMarkers = new ArrayList<Marker>();
	private static List<Marker> completedMarkers = new ArrayList<Marker>();
	
	ListMediator(){
	}
	
	// add to active list
	public void addToList(Marker tobeAdded){
		mMarkers.add(tobeAdded);
	}
	// add to completed list
	public void addToCompletedList(Marker tobeAdded){
		completedMarkers.add(tobeAdded);
	}
	
	// get Iterator from active list
	public Iterator getMarkerIterator(){
		return mMarkers.iterator();
	}
	// get Iterator from completed list
	public Iterator getCompletedMarkerIterator(){
		return completedMarkers.iterator();
	}
	
	// return the active list
	public static List<Marker> getList(){
		return mMarkers;
	}
	// return the completed list
	public static List<Marker> getCompletedList(){
		return completedMarkers;
	}
	
	// delete the active list
	public void deleteList(){
		//clear all markers and the list
		Iterator marker = mMarkers.iterator();
		while (marker.hasNext()) {
				Marker current = (Marker) marker.next();
				current.remove();
		}
		mMarkers.clear();
	}
	
	//remove marker at index, also remove it from the active list at index
	public void removeMarker(int index){
		Marker current = mMarkers.get(index);
		mMarkers.remove(index);
		current.remove();
	}
	
	//remove marker at index, also remove it from the completed list at index
	public void removeCompletedMarker(int index){
		Marker current = completedMarkers.get(index);
		completedMarkers.remove(index);
		current.remove();
	}
	
}
