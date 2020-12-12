package com.g10.locomobile.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class will implement
 */
public class User implements Serializable {


    private String name;
    private double wallet;
//    private ArrayList<Seat> boughtItem;
//    private ArrayList<User> friends;
    private String age;
    private String sex; // 0 for male, 1 for female
    private String type; // 0 for default , 1 for student and teacher, 2 for disabled, old people and veteran. 3 for officer. 4 for admin.
    private Long id;

    /**
     * Initializes the fields.
     * @param name
     * @param age
     * @param tckn
     * @param sex
     * @param type
     */
    public User(String name, String age, long tckn, String sex, String type){
        this(name,age,tckn,sex,type,20);

    }

    /**
     * Initializes the fields.
     * @param name
     * @param age
     * @param id
     * @param sex
     * @param type
     * @param wallet
     */
    public User(String name, String age, long id, String sex, String type,double wallet) {

        this.name = name;
        this.wallet = wallet;
        this.age = age;
        this.type = type;
        this.id = id;
        this.sex = sex;
    }

    /**
     * Initializes the fields.
     * @param name
     * @param boughtItem
     * @param friends
     * @param age
     * @param sex
     * @param type
     * @param tckn
     */
    public User(String name, ArrayList<Seat> boughtItem, ArrayList<User> friends, String age, String sex, String type, long tckn) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.type = type;
        this.id = tckn;
    }

    /**
     * For serializable interface.
     */
    public User(){

    }

    /**
     * Initializes the fields.
     * @param sex
     * @param tckn
     */
    public User(String sex, long tckn) {
        this.sex = sex;
        this.id = tckn;
    }

//    public void addFriend(User user) {
//
//        friends.add(user);
//    }
//
//    public void addItem(Seat seat) {
//        boughtItem.add(seat);
//    }
//
//    public void removeFriend(User user) {
//        friends.remove(user);
//
//    }

//    public void removeItem(Seat seat) {
//        boughtItem.remove(seat);
//    }

    /**
     * Deducts money from wallet.
     * @param amount amount
     */
    public void spend(double amount) {
        if (amount > 0)
            setWallet(getWallet() - amount);
    }

    /**
     * Adds money to wallet
     * @param amount amount
     */
    public void addAmount(double amount) {
        if (amount > 0)
            setWallet(getWallet() + amount);
    }

//
//    public void transferSeat(Seat seat, User otherUser) {
//        otherUser.addItem(seat);
//        this.removeItem(seat);
//    }

    /**
     *String representation of user.
     * @return String
     */
    @NonNull
    @Override
    public String toString() {
        String output;
        output = "Name: " + this.name;
        return output;
    }

    /**
     * Check TCKN number is valid or not
     * @param number tckn number
     * @return
     */
    public static boolean isValidTCKN(long number) {

        String textNumber = number + "";
        int total, evenTotal, oddTotal;
        if (textNumber.length() != 11)
            return false;
        if (textNumber.charAt(0) == '0')
            return false;
        total = 0;
        for (int i = 0; i < 10; i++) {
            total = total + Character.getNumericValue(textNumber.charAt(i));
        }
        if (total % 10 != Character.getNumericValue(textNumber.charAt(10)))
            return false;
        oddTotal = 0;
        evenTotal = 0;
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0)
                oddTotal = oddTotal + Character.getNumericValue(textNumber.charAt(i));
            else
                evenTotal = evenTotal + Character.getNumericValue(textNumber.charAt(i));
        }
        return ((oddTotal * 7 - evenTotal) % 10 == Character.getNumericValue(textNumber.charAt(9)));
    }

    /**
     * Compares the user
     * @param otherUser other user
     * @return result
     */
    public boolean equals(User otherUser) {
        return this.id == otherUser.id;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("wallet")
    public double getWallet() {
        return wallet;
    }

    @PropertyName("wallet")
    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

//    @PropertyName("boughtItem")
//    public ArrayList<Seat> getBuyedItem() {
//        return boughtItem;
//    }
//
//    @PropertyName("boughtItem")
//    public void setBuyedItem(ArrayList<Seat> buyedItem) {
//        this.boughtItem = buyedItem;
//    }
//
//    @PropertyName("boughtItem")
//    public void addtBuyedItem(Seat buyedItem) {
//        this.boughtItem.add(buyedItem);
//    }
//
//    @PropertyName("friends")
//    public ArrayList<User> getFriends() {
//        return friends;
//    }
//
//    @PropertyName("friends")
//    public void setFriends(ArrayList<User> friends) {
//        this.friends = friends;
//    }

    @PropertyName("age")
    public String getAge() {
        return age;
    }

    @PropertyName("age")
    public void setAge(String age) {
        this.age = age;
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }

    @PropertyName("type")
    public void setType(String type) {
        this.type = type;
    }

    @PropertyName("sex")
    public String getSex() {
        return sex;
    }

    @PropertyName("sex")
    public void setSex(String sex) {
        this.sex = sex;
    }

    @PropertyName("id")
    public void setid(long id) {
        this.id = id;
    }

    @PropertyName("id")
    public long getid() {
        return id;
    }
}
