package com.g10.locomobile.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

public class Wagon implements Serializable {

    private ArrayList<Seat> seats;
    private final int size = 36;
    private int type; // 0 for economy, 1 business, 2 sleeping, 3 dining.
    private int occupiedSeats;
    private double occupancyRate;
    private int id;
    private static int idCount = 0;

    /**
     * Initializes the fields.
     * @param seats
     * @param type
     */
    public Wagon(ArrayList<Seat> seats, int type) {
        this.seats = seats;
        this.type = type;
        calculateOccupiedSeatCount();
        this.occupiedSeats = 0;
        occupancyRate = getoccupancyRate();
    }

    @PropertyName("id")
    public int getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(int id) {
        this.id = id;
    }

    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int idCount) {
        Wagon.idCount = idCount;
    }

    /**
     * Initializes the fields.
     * @param type
     */
    public Wagon(int type) {
        seats = new ArrayList<Seat>();
        this.type = type;
        fillWagon();
        this.id = ++idCount;
    }

    /**
     * Serializable constructor
     */
    public Wagon() {
        seats = new ArrayList<Seat>();
        this.type = -1;
        fillWagon();
    }

    /**
     *String representation of wagon.
     * @return String
     */
    @Override
    public String toString() {
        return "Wagon " + id;
    }

    /**
     * Fills wagon with seats.
     */
    public void fillWagon() {
        while (seats.size() < size) {
            seats.add(new Seat());
        }

    }

    /**
     * Calculates occupied seat count.
     */
    public void calculateOccupiedSeatCount() {
        int count = 0;
        for (Seat seat : seats) {
            if (seat.isoccupied()) {
                count++;
            }
        }
        occupiedSeats = count;
    }

    /**
     *Checks whether wagon has specific user
     * @param user user
     * @return result
     */
    public boolean checkUser(User user) {
        for (Seat seat : seats) {
            if (seat.getowner() == user)
                return true;
        }
        return false;
    }

    /**
     * Finds user's seat in wagon
     * @param user user
     * @return seat.
     */
    public Seat isUserInWagon(User user) {
        for (Seat s : this.getseats()) {
            if (s.getowner() != null) {
                if (s.getowner().getid() == user.getid()) {
                    return s;
                }
            }
        }
        return null;
    }

    @PropertyName("occupancyRate")
    public void setoccupancyRate(double rate) {
        this.occupancyRate = rate;
    }

    @PropertyName("occupancyRate")
    public double getoccupancyRate() {

        return 100 * getoccupiedSeats() / (double) size;
    }

    @PropertyName("size")
    public int getSize() {
        return size;
    }

    @PropertyName("type")
    public int gettype() {
        return type;
    }

    @PropertyName("type")
    public void settype(int type) {
        this.type = type;
    }

    @PropertyName("seats")
    public void setseats(ArrayList<Seat> seats) {
        this.seats = seats;
    }

    @PropertyName("seats")
    public ArrayList<Seat> getseats() {
        return seats;
    }

    @PropertyName("occupiedSeats")
    public void setoccupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    @PropertyName("occupiedSeats")
    public int getoccupiedSeats() {
        calculateOccupiedSeatCount();
        return occupiedSeats;
    }

}
