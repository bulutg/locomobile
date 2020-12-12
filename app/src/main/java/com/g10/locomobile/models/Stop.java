package com.g10.locomobile.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * This class will be the stop in a line
 */
public class Stop implements Serializable {
    private String name;
//    private LocalTime time;

//    public Stop(String name,LocalTime time) {
//        this.name = name;
//        this.time = time;
//    }

    /**
     * Initializes the name
     * @param name name of the stop
     */
    public Stop(String name) {
        this.name = name;

    }

    /**
     * Empty constructor
     */
    public Stop() {
    }

    @Override
    /**
     * Demonstrates the name of the stop
     */
    public String toString() {
        return "Stop Name: " + name + " Arrival Time: ";
    }

    /**
     * Gets the name of the stop
     * @return the name of the stop
     */
    public String getName() {
        return name;
    }

    //    public void setTime(LocalTime time) {
//        this.time = time;
//    }
//
//    public LocalTime getTime() {
//        return time;
//    }
    @PropertyName("name")
    public void setname(String name) {
        this.name = name;
    }
}
