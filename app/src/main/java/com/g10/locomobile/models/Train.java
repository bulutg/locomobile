package com.g10.locomobile.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * Train class!
 */
public class Train implements Serializable {

    private String name;
    private ArrayList<Wagon> wagons;
    private int size;
    private Line line;
    private String date, hour;
    private int occupiedSeats;
    private double occupancyRate;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.#");


    /**
     * Constructors
     * creating a default train with 5 wagon
     * initializes String name and Line
     */
    public Train(String name) {
        this(name, 5, new Line()); // Default size is 5.
 }

    /**
     * empty constructor for serializable interface
     */
    public Train(){
    }

    /**
     * initializes String name and wagon size
     */
    public Train(String name, int size) {
        this.name = name;
        this.size = size;
        wagons = new ArrayList<Wagon>();
        line = new Line();
    }

    /**
     * initializes String name, wagon size, date of trip, hour of trip
     */
    public Train(String name, int size, String date, String hour) {
        this.name = name;
        this.size = size;
        this.hour = hour;
        this.date = date;
        wagons = new ArrayList<Wagon>();
        line = new Line();
        fillTrain();

    }

    /**
     * initializes String name, wagon size and Line
     */
    public Train(String Name, int Size, Line line) {
        wagons = new ArrayList<Wagon>();
        this.name = Name;
        this.size = Size;
        this.line = line;
        fillTrain();
    }

//    public Train(Train train) {
//        wagons = train.wagons;
//        this.name = train.name;
//        this.size = train.size;
//        this.line = train.line;
//    }



    /**
     * initializes String name, wagon size, wagons and Line
     */
    public Train(String Name, int Size, ArrayList<Wagon> Wagons, Line line) {
        this.name = Name;
        this.size = Size;
        this.wagons = Wagons;
        this.line = line;
    }
    /**
     * Checking train has a stop or not
     * @param stop checking the stop
     * @return line.isSameStopName(stop) checking with another boolean method
     */
    public boolean hasStop(Stop stop) {
        return line.isSameStopName(stop);
    }

    /**
     * checking the stops has correct order
     * @param stopOne first stop input
     * @param stopTwo second stop input
     * @return line.compareStops(stopOne, stopTwo) > 0 returning true if it is bigger than 0
     */
    public boolean hasCorrectOrder(Stop stopOne, Stop stopTwo) {
        return line.compareStops(stopOne, stopTwo) > 0;
    }


    @NonNull
    @Override
    /**
     * toString method to write train informations
     * @return output the informations
     */

    public String toString() {
        String output;
        output = "Train: " + name + " (%" + decimalFormat.format(getOccupancyRate() )+ ") Date: " + date + " " + hour;
        return output;
    }

    @Override
    /**
     * checking the same train exists or not
     * @param o object o
     * @return this.name.equals(t.getname()) to get a boolean
     */
    public boolean equals(Object o) {
        if (o instanceof Train) {
            Train t = (Train) o;
            return this.name.equals(t.getname());
        } else
            return false;
    }

    /**
     * getting seat count
     * @return count seat number
     */
    public int getSeatCount() {
        int count = 0;
        for (Wagon wagon : wagons) {
            count += wagon.getSize();
        }
        return count;
    }

    /**
     * getting filling train
     */
    public void fillTrain() {
        while (wagons.size() < size)
            wagons.add(new Wagon(0));
    }

    /**
     * checking the user
     * @param user User
     * @return true if user exists in wagon
     */
    public boolean checkUser(User user) {
        for (Wagon wagon : wagons) {
            if (wagon.checkUser(user))
                return true;
        }
        return false;
    }

    /**
     * getting the size of occupied seats
     * @return count occupied seat count
     */
    public int calculateOccupiedSeats() {
        int count = 0;
        for (Wagon wagon : wagons) {
            count = count + wagon.getoccupiedSeats();
        }
        occupiedSeats = count;
        return count;
    }

    /**
     * calculates rates of occupancy
     * @return rates occupancy rate
     */
    public double calculateOccupancyRate() {
        double rates = 0;
        for (Wagon wagon : wagons) {
            rates += wagon.getoccupancyRate();
        }
        occupancyRate = rates / wagons.size();
        return rates / wagons.size();
    }

    /**
     * adding wagon
     * @param wagon wagon that will be added
     */
    public void addWagon(Wagon wagon) {

        wagons.add(wagon);

    }

    /**
     * checking user in the train or not
     * @param user to check the user
     * @return result if user in the train it is true else it is false
     */
    public Wagon isUserInTrain(User user){
        for (Wagon w: this.getwagons()){
            if (w.isUserInWagon(user) != null){
                return w;
            }
        }
        return null;
    }

    /**
     * getting the occupancy rate
     * @return calculateOccupancyRate()
     */
    public double getOccupancyRate() {

        return calculateOccupancyRate();
    }

    /**
     * getting the occupied seats
     * @return calculateOccupiedSeats()
     */
    public int getOccupiedSeats() {

        return calculateOccupiedSeats();
    }
    /**
     * removing a wagon
     * @param wagon wagon that will be removed
     */
    public void removeWagon(Wagon wagon) {
        wagons.remove(wagon);
    }

    @PropertyName("name")
    public String getname() {
        return name;
    }

    @PropertyName("name")
    public void setname(String name) {
        this.name = name;
    }

    @PropertyName("size")
    public void setsize(int size) {
        this.size = size;
    }

    @PropertyName("size")
    public int getsize() {
        return size;
    }

    @PropertyName("wagons")
    public ArrayList<Wagon> getwagons() {
        return wagons;
    }

    @PropertyName("wagons")
    public void setwagons(ArrayList<Wagon> Wagons) {
        this.wagons = Wagons;
    }

    @PropertyName("line")
    public void setline(Line TrainLine) {//setter metodlar bence bunlarda commente gerek yok
        this.line = TrainLine;
    }

    @PropertyName("line")
    public Line getline() {
        return line;
    }

    @PropertyName("date")
    public void setdate(String date) {
        this.date = date;
    }

    @PropertyName("date")
    public String getdate() {
        return date;
    }

    @PropertyName("hour")
    public void sethour(String hour) {
        this.hour = hour;
    }

    @PropertyName("hour")
    public String gethour() {
        return hour;
    }

}
