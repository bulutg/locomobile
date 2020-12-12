package com.g10.locomobile.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * This class will calculate the price
 */
public class Ticket implements Serializable {
    private Line line;
    private double price;
    private Stop start;
    private Stop stop;
    private Seat seat;

    /**
     * Initializes the Line of the ticket, Seat of the ticket, start Stop, stop Stop, and price
     * @param line line of the ticket
     * @param seat seat of the ticket
     * @param start start position of the line
     * @param stop stop position of the line
     * @param price price of the ticket
     */
    public Ticket(Line line, Seat seat, Stop start, Stop stop, double price) {
        this.line = line;
        this.start = start;
        this.stop = stop;
        this.seat = seat;
        this.price = price;
        calculatePrice();
    }

    /**
     * Initializes Seat of the ticket, start Stop, stop Stop, and price
     * @param seat seat of the ticket
     * @param start start position of the line
     * @param stop stop position of the line
     * @param price price of the ticket
     */
    public Ticket(Seat seat, Stop start, Stop stop, double price) {
        this.start = start;
        this.stop = stop;
        this.seat = seat;
        this.price = price;
        calculatePrice();
    }
    /**
     * Empty Constructor
     */
    public Ticket(){

    }


    @NonNull
    @Override
    public String toString() {
        String txt;
        txt = "Cost: " + getPrice();

        return txt;
    }

    /**
     * Calculates the price
     */
    public void calculatePrice() {

        double discountRate = 1;
        if (seat.getowner().getType().equals("education"))
            discountRate -= 0.3;
        else if (seat.getowner().getType().equals("disabled"))
            discountRate = 0;
        discountRate *= getLineUsage();
        if (discountRate > 0)
            setPrice(getPrice() * discountRate);
        else
            setPrice(0);
    }

    /**
     * Gets the line usage
     * @return the distance between two stops that a user uses
     */
    public double getLineUsage() {
        return (double) line.compareStops(start, stop) / (line.getLineLength() - 1);
    }

    @PropertyName("seat")
    public Seat getSeat() {
        return seat;
    }

    @PropertyName("seat")
    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    @PropertyName("line")
    public Line getLine() {
        return line;
    }

    @PropertyName("line")
    public void setLine(Line line) {
        this.line = line;
    }

    @PropertyName("price")
    public void setPrice(double price) {
        this.price = price;
    }

    @PropertyName("price")
    public double getPrice() {
        return price;
    }

    @PropertyName("start")
    public void setStart(Stop start) {
        this.start = start;
    }

    @PropertyName("start")
    public Stop getStart() {
        return start;
    }

    @PropertyName("stop")
    public Stop getStop() {
        return stop;
    }

    @PropertyName("stop")
    public void setStop(Stop stop) {
        this.stop = stop;
    }
}
