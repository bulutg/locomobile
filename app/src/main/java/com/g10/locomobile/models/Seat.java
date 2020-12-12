package com.g10.locomobile.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

/**
 * This class will be the seat object in the wagon
 */
public class Seat implements Serializable {

    private int type;
    private boolean isOccupied;
    private User owner;
    private int id;
    private static int idCount = 0;
    private static String letters = "ABCDEFGHI";

    /**
     * No argument constructor
     */
    public Seat() {
        this(0,false,null,0);
    }

    /**
     * Initializes type, isOccupied, owner, and id
     * @param type type of the seat
     * @param isOccupied whether the seat is occupied or not
     * @param owner the owner of the seat
     * @param id id of the seat
     */
    public Seat(int type, boolean isOccupied, User owner,int id) {
        this.type = type;
        this.isOccupied = isOccupied;
        this.owner = owner;
        this.id = idCount;
        idCount++;
    }

    /**
     * Find location of seat
     * @param id seat id
     * @return location
     */
    public static String findSeatLocation(int id){

        String txt;
        txt = "" + letters.charAt(id / 4);

        txt += ((id) % 4) + 1;
        return txt;
    }

    @Override
    public String toString() {
        return "Seat Number: " + findSeatLocation(getid());
    }

    @PropertyName("type")
    public int gettype() {
        return type;
    }

    @PropertyName("type")
    public void settype(int type) {
        this.type = type;
    }

    @PropertyName("occupied")
    public boolean isoccupied() {
        return isOccupied;
    }

    @PropertyName("occupied")
    public void setoccupied(boolean occupied) {
        isOccupied = occupied;
    }

    @PropertyName("owner")
    public User getowner() {
        return owner;
    }

    @PropertyName("owner")
    public void setowner(User owner) {

        if (owner != null)
            setoccupied(true);
           this.owner = owner;
    }

    @PropertyName("id")
    public int getid() {
        return id;
    }

    @PropertyName("id")
    public void setowner(int id) {
        this.id = id;
    }


}
