package com.g10.locomobile.models;

import java.time.LocalTime;
import java.util.ArrayList;

public class Demo {

    public static void main(String[] args) {
        Line line = new Line();

//        Stop stop1 = new Stop("Yov",LocalTime.of(18,16,0));
//        Stop stop2 = new Stop( "vov",LocalTime.now());
//        Stop stop3 = new Stop( "vov", LocalTime.now());

        Stop stop1 = new Stop("Yov");
        Stop stop2 = new Stop("vov");
        Stop stop3 = new Stop("vov");

        line.addStop(stop1);
        line.addStop(stop2);
        line.addStop(stop3);

        ArrayList<Train> trains = new ArrayList<Train>();
        for (int i = 0; i < 5; i++) {
            trains.add(new Train("Train" + i, i + 3, line));
        }
        trains.add(new Train("Yoyo", 12));


//        User user1 = new User("Mahmut","132","Mahmutxx",31,12345678910l,false);
//
//        Seat seat = trains.get(0).getwagons().get(0).getseats().get(0);
//
//        seat.setOwner(user1);
//        user1.addItem(new Ticket(line,seat,stop1,stop3,30));
//
////        System.out.println(TrainSystem.remaingTime(stop1));
//        System.out.println(user1.getBuyedItem());
//        System.out.println(trains.get(0).hasStop(new Stop("Yov",LocalTime.now())));

        System.out.println(trains.get(0).hasCorrectOrder(stop2, stop2));

        System.out.println(TrainSystem.searchTrain(stop1, stop2, trains, "test"));


    }
}