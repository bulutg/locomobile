package com.g10.locomobile.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Line class!
 */
public class Line implements Serializable {

    //properties
    private ArrayList<Stop> stops;
    private Stop destination;
    private Stop start;

    //constructors

    /**
     * Initializes the stops array list.
     */
    public Line() {
        stops = new ArrayList<Stop>();
    }

    /**
     * Inıtializes the array list of Stop objects according to the given parameter.
     * Refreshes the limit of the array list of Stop objects.
     * @param stops the array list of Stop objects
     */
    public Line(ArrayList<Stop> stops) {
        this.stops = stops;
        refreshLimit();
    }


    @Override
    /**
     * Demonstrates the start stop and the destination stop
     */
    public String toString() {
        String text;
        text = "Start: " + start + " Stop: " + destination;
        return text;
    }

    /**
     * Gets the length of the line
     * @return the size of the stops array list
     */
    public int getLineLength() {
        return stops.size();
    }

    /**
     * Compares the given Stop objects according to the indexes of them in the stops list.
     * @param stopOne the first Stop object that will be compared
     * @param stopTwo the second Stop object that will be compared
     * @return the difference between indexes of stops
     */
    public int compareStops(Stop stopOne, Stop stopTwo) {

        return stops.indexOf(stopTwo) - stops.indexOf(stopOne);
    }

    /**
     * Finds the Stop object that has the given String as a name in the array list of Stop objects
     * @param stop the name of the Stop object
     * @return the Stop object that has the given String if that object is contained in the list, null if not
     */
    public Stop findStop(String stop) {
        for (Stop item : stops) {
            if (item.getName().equals(stop))
                return item;
        }
        return null;
    }

    /**
     * Refreshes the start and destination references according to the last and the first index of the list stops
     */
    public void refreshLimit() {
        start = stops.get(0);
        destination = stops.get(stops.size() - 1);
    }

    /**
     * Adds new stop to the list of Stop objects if the list does not contain the given object
     * @param stop the object that will be added to the list
     */
    public void addStop(Stop stop) {
        if (!stops.contains(stop)) {
            stops.add(stop);
            refreshLimit();
        }
    }

    /**
     * Swaps the Stop objects in the stops list.
     * @param stopOne the first Stop object that will be swapped
     * @param stopTwo the second Stop object that will be swapped
     */
    public void swapStops(Stop stopOne, Stop stopTwo) {
        if (stops.contains(stopOne) && stops.contains(stopTwo)) {
            int indexOne = stops.indexOf(stopOne);
            int indexTwo = stops.indexOf(stopTwo);
            Stop tmp = stops.get(indexTwo);
            stops.set(indexTwo, stops.get(indexOne));
            stops.set(indexOne, tmp);
            refreshLimit();
        }
    }

    /**
     * Removes the given stop and refreshes the limits in the list
     * @param stop the Stop object that will be removed
     */
    public void removeStop(Stop stop) {
        stops.remove(stop);
        refreshLimit();
    }

    /**
     * Checks if the given stop object exists in the list
     * @param stop the Stop object that will be checked
     * @return true if the given object exists in the list, false if not
     */
    public boolean isSameStopName(Stop stop) {
        if (!stops.isEmpty()) {
            for (Stop item : stops) {
                if (item.getName().equalsIgnoreCase(stop.getName()))
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if the stop object that has the given string name exists in the list
     * @param stop the stop object that will be checked
     * @return true if the object that has the given string exists in the list, false if not
     */
    public boolean isSameStopName(String stop) {
        if (stops != null) {
            for (Stop item : stops) {
                if (item.getName().equalsIgnoreCase(stop))
                    return true;
            }
        }
        return false;

    }

    @PropertyName("stops")
    public ArrayList<Stop> getstops() {
        return stops;
    }

    @PropertyName("stops")
    public void setstops(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    @PropertyName("destination")
    public Stop getdestination() {
        return destination;
    }

    @PropertyName("destination")
    public void setdestination(Stop destination) {
        this.destination = destination;
    }

    @PropertyName("start")
    public Stop getstart() {
        return start;
    }

    @PropertyName("start")
    public void setstart(Stop start) {
        this.start = start;
    }
}
